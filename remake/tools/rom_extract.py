#!/usr/bin/env python3
import argparse, hashlib, json, pathlib, re

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
    # stay inside the verified 0..48 room range, then emit candidates for
    # comparison against the loader/disassembly.
    candidates=[]
    record_size=10
    for start in range(0, len(prg)-record_size*12):
        records=[]
        pos=start
        while pos+record_size <= len(prg):
            r=prg[pos:pos+record_size]
            area, sub=r[0], r[2]
            if area > 48 or sub > 48:
                break
            sprite=r[4] | (r[5]<<8)
            desc=r[7] | (r[8]<<8)
            if sprite < 0x6000 or desc < 0x6000:
                break
            records.append({
                "prg_offset":pos, "area_id":area, "x":r[1],
                "sub_area_id":sub, "y":r[3],
                "sprite_address":sprite, "interaction_id":r[6],
                "description_address":desc, "interaction_sub_id":r[9]
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
            o=r["offset"]
            r["raw_hex"]=prg[o:o+10].hex()

    # Add structural evidence without promoting candidates to verified data.
    # Addresses in fields 4/5 and 7/8 consistently land in the CPU ROM window;
    # this is useful evidence for reverse engineering, but still not semantic proof.
    for c in filtered:
        rs=c["records"]
        c["structure"]={
            "area_ids":sorted(set(r["area_id"] for r in rs)),
            "sprite_address_min":min(r["sprite_address"] for r in rs),
            "sprite_address_max":max(r["sprite_address"] for r in rs),
            "description_address_min":min(r["description_address"] for r in rs),
            "description_address_max":max(r["description_address"] for r in rs),
            "all_sprite_addresses_in_cpu_rom_window":all(0x8000 <= r["sprite_address"] <= 0xffff for r in rs),
            "all_description_addresses_in_cpu_rom_window":all(0x8000 <= r["description_address"] <= 0xffff for r in rs)
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
