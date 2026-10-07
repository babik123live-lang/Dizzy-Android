#!/usr/bin/env python3
import argparse, hashlib, json, pathlib, re


def decode_de84_stream(prg, bank, src_cpu, count):
    """Decode the game's $DE84 compressed CHR stream into 16-byte NES tiles."""
    if not (0 <= bank < len(prg)//0x4000):
        raise ValueError("bank out of range")
    if not (0x8000 <= src_cpu <= 0xBFFF):
        raise ValueError("source CPU address out of switchable-bank range")
    bank_data=prg[bank*0x4000:(bank+1)*0x4000]
    p=src_cpu-0x8000
    tiles=[]
    trace=[]
    for block in range(count):
        block_start=p
        header=bank_data[p]; p+=1
        repeat=header & 0x0F
        literal_count=(header >> 4) + 1
        remaining=16
        out_bytes=[]
        last=None
        for _ in range(literal_count):
            if remaining == 0: break
            last=bank_data[p]; p+=1
            out_bytes.append(last); remaining-=1
        if remaining and last is not None:
            extra=remaining if repeat == 0 else min(repeat,remaining)
            out_bytes.extend([last]*extra); remaining-=extra
        while remaining:
            last=bank_data[p]; p+=1
            out_bytes.append(last); remaining-=1
        if len(out_bytes) != 16:
            raise AssertionError("DE84 block did not decode to 16 bytes")
        tiles.append(bytes(out_bytes))
        trace.append({
            "block":block,
            "source_cpu_start":0x8000+block_start,
            "source_cpu_end_exclusive":0x8000+p,
            "header":header,
            "literal_count":literal_count,
            "repeat_nibble":repeat
        })
    return tiles,trace,0x8000+p

def decode_nes_2bpp(tile):
    if len(tile) != 16: raise ValueError("NES tile must be 16 bytes")
    rows=[]
    for y in range(8):
        p0,p1=tile[y],tile[y+8]
        rows.append([((p0>>bit)&1) | (((p1>>bit)&1)<<1) for bit in range(7,-1,-1)])
    return rows

def extract_chr_resource_table(prg):
    # $DD55-$DDE0 in the fixed bank: 28 five-byte descriptors consumed by $DDE1.
    start=15*0x4000+(0xDD55-0xC000)
    raw=prg[start:start+28*5]
    rows=[]
    for i in range(28):
        lo,hi,dest,count,flags=raw[i*5:(i+1)*5]
        rows.append({
            "index":i,
            "source_bank":flags & 0x0F,
            "pattern_table":1 if flags & 0x80 else 0,
            "source_cpu":lo | (hi<<8),
            "destination_tile":dest,
            "tile_count":count,
            "flags_raw":flags
        })
    return rows

def main():
    ap=argparse.ArgumentParser()
    ap.add_argument("rom")
    ap.add_argument("-o","--out",default="remake/generated")
    a=ap.parse_args()
    raw=pathlib.Path(a.rom).read_bytes()
    if raw[:4] != b"NES\x1a": raise SystemExit("not iNES")
    prg_size=raw[4]*16384; chr_size=raw[5]*8192
    mapper=(raw[6]>>4)|(raw[7]&0xF0)
    prg=raw[16:16+prg_size]
    out=pathlib.Path(a.out); out.mkdir(parents=True,exist_ok=True)
    meta={"sha256":hashlib.sha256(raw).hexdigest(),"size":len(raw),"prg_size":prg_size,"chr_size":chr_size,"mapper":mapper}
    (out/"rom.json").write_text(json.dumps(meta,indent=2),encoding="utf-8")

    resources=extract_chr_resource_table(prg)
    (out/"chr_resource_table.json").write_text(json.dumps(resources,indent=2),encoding="utf-8")
    # Resource 9 is proven by $DDE1 and its call site to populate pattern table 1
    # from tile $4D. Decode the exact frames later emitted as OAM tiles $4F-$52.
    r9=resources[9]
    decoded,trace,source_end=decode_de84_stream(
        prg,r9["source_bank"],r9["source_cpu"],r9["tile_count"])
    wanted=[]
    for tile_no in range(0x4F,0x53):
        idx=tile_no-r9["destination_tile"]
        tile=decoded[idx]
        wanted.append({
            "tile":tile_no,
            "raw_hex":tile.hex(),
            "pixels_2bpp":decode_nes_2bpp(tile),
            "decode_trace":trace[idx]
        })
    chr_evidence={
        "resource_index":9,
        "resource":r9,
        "source_cpu_end_exclusive":source_end,
        "tiles":wanted
    }
    (out/"chr_tiles_4f_52.json").write_text(
        json.dumps(chr_evidence,indent=2),encoding="utf-8")

    # Tri-lingual room prose starts in the later PRG banks. Keep offsets so the
    # remake can replace strings without depending on the NES renderer.
    strings=[]
    for m in re.finditer(rb"[A-Z][A-Z0-9 ,.!?'\-]{7,}",prg):
        s=m.group().decode("ascii","replace").strip()
        if len(s)>=8 and ("YOU " in s or "CARRY" in s or "DIZZY" in s or "HUT" in s):
            strings.append({"prg_offset":m.start(),"text":s})
    (out/"english_strings.json").write_text(json.dumps(strings,indent=2,ensure_ascii=False),encoding="utf-8")

    # Persistent objects are 10-byte records in the original game.  Do not
    # guess their table location: scan for long runs whose area/sub-area bytes
    # fit conservative byte/address constraints, then emit candidates for
    # comparison against the loader/disassembly.
    candidates=[]
    record_size=10
    for start in range(0, len(prg)-record_size*12):
        records=[]
        pos=start
        while pos+record_size <= len(prg):
            r=prg[pos:pos+record_size]
            raw0, raw2=r[0], r[2]
            if raw2 > 48:
                break
            sprite=r[4] | (r[5]<<8)
            desc=r[7] | (r[8]<<8)
            if sprite < 0x6000 or desc < 0x6000:
                break
            records.append({
                "prg_offset":pos, "raw0":raw0, "raw1":r[1],
                "raw2":raw2, "raw3":r[3],
                "raw45le":sprite, "raw6":r[6],
                "raw78le":desc, "raw9":r[9]
            })
            pos += record_size
        if len(records) >= 12:
            candidates.append({"start_prg_offset":start,"count":len(records),"records":records})
    # Remove overlapping suffixes: retain the longest candidate for each run.
    candidates.sort(key=lambda c:(c["start_prg_offset"],-c["count"]))
    filtered=[]
    covered_until=-1
    for c in candidates:
        end=c["start_prg_offset"]+c["count"]*record_size
        if c["start_prg_offset"] < covered_until:
            continue
        filtered.append(c); covered_until=end
    # Verify candidate table addresses only when preceded by a 6502 opcode that
    # actually consumes a 16-bit absolute operand. A raw byte pair is not proof.
    abs_ops={0x20:"JSR",0x4c:"JMP",0xad:"LDA",0xbd:"LDA_X",0xb9:"LDA_Y",
             0x8d:"STA",0x9d:"STA_X",0x99:"STA_Y",0xae:"LDX",0xac:"LDY",
             0xcd:"CMP",0xed:"SBC",0x6d:"ADC",0x2d:"AND",0x3d:"AND_X",0x39:"AND_Y",0x0d:"ORA",0x1d:"ORA_X",0x19:"ORA_Y",0x4d:"EOR",0x5d:"EOR_X",0x59:"EOR_Y"}
    for c in filtered:
        cpu_addr=0x8000 + (c["start_prg_offset"] & 0x7fff)
        lo,hi=cpu_addr & 0xff,(cpu_addr>>8)&0xff
        refs=[]
        for i in range(len(prg)-2):
            if prg[i] in abs_ops and prg[i+1]==lo and prg[i+2]==hi:
                refs.append({"prg_offset":i,"opcode":abs_ops[prg[i]],"cpu_address":f"0x{cpu_addr:04X}"})
        c["absolute_6502_refs"]=refs
        c["verified_by_code_reference"]=bool(refs)
    # Preserve exact record bytes so generated evidence can be compared byte-for-byte
    # against the source ROM rather than reconstructed from interpreted fields.
    for c in filtered:
        for r in c["records"]:
            o=r["prg_offset"]
            r["raw_hex"]=prg[o:o+10].hex()

    # Add structural evidence without promoting candidates to verified data.
    # Addresses in fields 4/5 and 7/8 consistently land in the CPU ROM window;
    # this is useful evidence for reverse engineering, but still not semantic proof.
    for c in filtered:
        rs=c["records"]
        c["structure"]={
            "raw0_values":sorted(set(r["raw0"] for r in rs)),
            "raw45le_min":min(r["raw45le"] for r in rs),
            "raw45le_max":max(r["raw45le"] for r in rs),
            "raw78le_min":min(r["raw78le"] for r in rs),
            "raw78le_max":max(r["raw78le"] for r in rs),
            "all_raw45le_in_cpu_rom_window":all(0x8000 <= r["raw45le"] <= 0xffff for r in rs),
            "all_raw78le_in_cpu_rom_window":all(0x8000 <= r["raw78le"] <= 0xffff for r in rs)
        }

    # Record every raw little-endian occurrence of each candidate start,
    # including surrounding bytes. This separates reproducible byte evidence
    # from instruction-level proof and helps identify pointer tables/encoded data.
    for c in filtered:
        addr=0x8000 + (c["start_prg_offset"] & 0x7fff)
        needle=bytes((addr & 0xff,(addr>>8)&0xff))
        hits=[]
        pos=0
        while True:
            pos=prg.find(needle,pos)
            if pos < 0: break
            lo=max(0,pos-12); hi=min(len(prg),pos+14)
            hits.append({"prg_offset":pos,"context_hex":prg[lo:hi].hex()})
            pos += 1
        c["raw_pointer_byte_occurrences"]=hits

    (out/"persistent_object_candidates.json").write_text(
        json.dumps(filtered,indent=2,ensure_ascii=False),encoding="utf-8")
if __name__=="__main__": main()
