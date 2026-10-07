#!/usr/bin/env python3
"""Decode known 10-byte FAoD persistent-object records from a supplied NES ROM."""
import argparse,json,pathlib
from ines import parse

# Verified anchors documented against the European NES build.
KNOWN={
  "star_plant":0x03F736,
  "plank":0x03F952,
}
def file_to_prg(file_offset:int)->int:
    return file_offset-16

def decode(prg,o):
    b=prg[o:o+10]
    return {
      "area_id":b[0],"x":b[1],"sub_area_id":b[2],"y":b[3],
      "sprite_address":b[4]|b[5]<<8,
      "interaction_id":b[6],
      "description_address":b[7]|b[8]<<8,
      "interaction_sub_id":b[9],
      "raw":" ".join(f"{x:02X}" for x in b)
    }
def main():
    ap=argparse.ArgumentParser();ap.add_argument("rom");ap.add_argument("-o","--out",default="remake/generated/known_objects.json");a=ap.parse_args()
    ines=parse(pathlib.Path(a.rom).read_bytes())
    out={}
    for name,fo in KNOWN.items():
        po=file_to_prg(fo)
        out[name]={"file_offset":fo,"prg_offset":po,**decode(ines.prg,po)}
    p=pathlib.Path(a.out);p.parent.mkdir(parents=True,exist_ok=True);p.write_text(json.dumps(out,indent=2),encoding="utf-8")
if __name__=="__main__":main()
