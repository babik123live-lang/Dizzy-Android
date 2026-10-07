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
if __name__=="__main__": main()
