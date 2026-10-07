#!/usr/bin/env python3
"""Search the supplied ROM for externally documented 10-byte object signatures.
Never assumes offsets from another ROM revision."""
import argparse,json,pathlib
from ines import parse
SIGS={
 "star_plant":bytes.fromhex("0F 2F 08 9E 63 8B 02 C8 82 03"),
 "plank":bytes.fromhex("0E F4 01 A8 5E A5 1D F4 83 00"),
}
def main():
 ap=argparse.ArgumentParser();ap.add_argument("rom");ap.add_argument("-o","--out",default="remake/generated/object_signature_search.json");a=ap.parse_args()
 prg=parse(pathlib.Path(a.rom).read_bytes()).prg; result={}
 for name,sig in SIGS.items():
  hits=[]; p=0
  while True:
   p=prg.find(sig,p)
   if p<0: break
   hits.append(p);p+=1
  result[name]={"signature":sig.hex(" ").upper(),"prg_offsets":hits}
 pathlib.Path(a.out).write_text(json.dumps(result,indent=2),encoding="utf-8")
 print(result)
if __name__=="__main__":main()
