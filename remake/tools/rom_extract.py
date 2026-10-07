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
    (out/"persistent_object_candidates.json").write_text(
        json.dumps(filtered,indent=2,ensure_ascii=False),encoding="utf-8")
if __name__=="__main__": main()
