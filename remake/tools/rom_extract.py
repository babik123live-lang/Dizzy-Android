#!/usr/bin/env python3
import argparse, hashlib, json, pathlib, re


def decode_de84_stream(prg, bank, src_cpu, count):
    """Byte-faithful model of the game's $DE84 CHR decoder."""
    if not (0 <= bank < len(prg)//0x4000):
        raise ValueError("bank out of range")
    if not (0x8000 <= src_cpu <= 0xBFFF):
        raise ValueError("source CPU address out of switchable-bank range")
    bank_data=prg[bank*0x4000:(bank+1)*0x4000]
    p=src_cpu-0x8000
    tiles=[]; trace=[]
    for block in range(count):
        block_start=p
        header=bank_data[p]; p+=1
        repeat=header & 0x0f
        literal=(header>>4)+1
        x=16
        out=[]
        while True:
            a=bank_data[p]
            out.append(a)
            literal=(literal-1)&0xff
            if literal != 0:
                x=(x-1)&0xff
                if x:
                    p+=1
                    continue
                p+=1
                break
            x=(x-1)&0xff
            if not x:
                p+=1
                break
            out.append(a)
            repeat=(repeat-1)&0xff
            while repeat != 0:
                x=(x-1)&0xff
                if not x:
                    p+=1
                    break
                out.append(a)
                repeat=(repeat-1)&0xff
            if not x:
                break
            x=(x-1)&0xff
            if x:
                p+=1
                continue
            p+=1
            break
        if len(out) != 16:
            raise AssertionError((hex(0x8000+block_start),hex(header),len(out)))
        tiles.append(bytes(out))
        trace.append({
            "block":block,
            "source_cpu_start":0x8000+block_start,
            "source_cpu_end_exclusive":0x8000+p,
            "header":header
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

    # Verified 5-byte CHR upload descriptors consumed by DDE1/DE84.
    desc_cpu=0xDD55
    desc_count=28
    fixed_base=len(prg)-0x4000
    desc_off=fixed_base+(desc_cpu-0xC000)
    desc_blob=prg[desc_off:desc_off+desc_count*5]
    chr_desc=[]
    for i in range(desc_count):
        d=desc_blob[i*5:(i+1)*5]
        src=d[0] | (d[1]<<8)
        ctrl=d[4]
        bank=ctrl & 0x0f
        pattern=(ctrl>>7)&1
        src_off=bank*0x4000+(src-0x8000) if 0x8000 <= src < 0xC000 else None
        chr_desc.append({
            "index":i,
            "source_cpu":src,
            "source_bank_raw":bank,
            "source_prg_offset":src_off,
            "destination_tile":d[2],
            "tile_count":d[3],
            "pattern_table":pattern,
            "control_raw":ctrl,
        })
    (out/"chr_upload_descriptors.json").write_text(
        json.dumps(chr_desc,indent=2),encoding="utf-8")

    # Descriptor 23 is directly requested by bank 14 at CPU $9158.
    # It uploads tiles $4F-$52 as four literal 16-byte NES 2bpp patterns.
    star_desc=chr_desc[23]
    if not (star_desc["source_bank_raw"]==7 and
            star_desc["source_cpu"]==0xB33C and
            star_desc["destination_tile"]==0x4F and
            star_desc["tile_count"]==4 and
            star_desc["pattern_table"]==0):
        raise SystemExit("unexpected tile 4F-52 descriptor")
    p=star_desc["source_prg_offset"]
    tiles=[]
    for tile_no in range(0x4F,0x53):
        header=prg[p]
        if header != 0xF0:
            raise SystemExit("tile 4F-52 stream is no longer literal F0 form")
        b=prg[p+1:p+17]
        if len(b)!=16:
            raise SystemExit("truncated tile stream")
        plane0=b[:8]; plane1=b[8:]
        pixels=[
            [((plane0[y]>>(7-x))&1) | (((plane1[y]>>(7-x))&1)<<1)
             for x in range(8)]
            for y in range(8)
        ]
        tiles.append({
            "tile":tile_no,
            "stream_header":header,
            "bytes_hex":b.hex(),
            "pixels":pixels,
        })
        p += 17
    evidence={
        "descriptor_index":23,
        "direct_call":{"bank":14,"cpu_address":"0x9158","immediate_index":23},
        "source_bank_raw":7,
        "source_cpu":"0xB33C",
        "source_prg_offset":star_desc["source_prg_offset"],
        "pattern_table":0,
        "ppu_range":"0x04F0-0x052F",
        "tiles":tiles,
    }
    (out/"tile_4f_52_evidence.json").write_text(
        json.dumps(evidence,indent=2),encoding="utf-8")

    # The animated $4F-$52 placements are the collectible stars.
    # Main path: 210 grouped horizontal placements. Three additional paths
    # contribute 10 + 20 + 10 placements, giving the exact 250-star counter.
    def bank_byte(bank,cpu):
        if bank==15:
            if not 0xC000 <= cpu <= 0xFFFF: raise ValueError(hex(cpu))
            po=fixed_base+(cpu-0xC000)
        else:
            if not 0x8000 <= cpu <= 0xBFFF: raise ValueError((bank,hex(cpu)))
            po=bank*0x4000+(cpu-0x8000)
        return prg[po]

    stars=[]
    for i in range(210):
        key=bank_byte(13,0xB312+i)
        world=bank_byte(13,0xB3E5+i) | (bank_byte(13,0xB4B8+i)<<8)
        fixed=bank_byte(13,0xB58B+i)
        stars.append({
            "id":i,
            "layout":"horizontal",
            "group_raw":key,
            "world_axis":world,
            "fixed_axis":fixed,
            "state_bit":i,
        })
    if bank_byte(13,0xB312+210) != 0xFF:
        raise SystemExit("210-entry star table terminator changed")

    supplemental=[
        (10,0x8F43,10,210,"vertical_a"),
        (13,0x8CFE,20,220,"vertical_b"),
        (14,0x9260,10,240,"vertical_c"),
    ]
    for bank,cpu,count,start_id,label in supplemental:
        for n in range(count):
            p=cpu+3*n
            world=bank_byte(bank,p) | (bank_byte(bank,p+1)<<8)
            fixed=bank_byte(bank,p+2)
            stars.append({
                "id":start_id+n,
                "layout":label,
                "world_axis":world,
                "fixed_axis":fixed,
                "state_bit":start_id+n,
            })

    if len(stars)!=250 or [s["id"] for s in stars] != list(range(250)):
        raise SystemExit("star placement coverage is not exactly 0..249")
    state_map=[bank_byte(15,0xC200+i) for i in range(250)]
    if not all(state_map[i]==i//8 for i in range(250)):
        raise SystemExit("packed star-state map changed")
    counter_init=bytes(bank_byte(15,a) for a in range(0xF8E1,0xF8E6))
    if counter_init != bytes((0xA9,0xFA,0x8D,0x72,0x07)):
        raise SystemExit("250-star counter initialization changed")

    star_manifest={
        "total":250,
        "counter_ram":"0x0772",
        "counter_initial":250,
        "packed_state_ram":"0x0751-0x0770",
        "main_count":210,
        "supplemental_counts":[10,20,10],
        "animation_tiles":[0x4F,0x50,0x51,0x52,0x51,0x50,0x4F],
        "graphics_resource_id":23,
        "placements":stars,
    }
    (out/"star_manifest.json").write_text(
        json.dumps(star_manifest,indent=2),encoding="utf-8")
if __name__=="__main__": main()
