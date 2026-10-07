#!/usr/bin/env python3
import argparse,json,pathlib
from ines import parse

START=0x19738
LANGS=("en","fr","de")

def read_text(prg,pos):
    start=pos; out=[]
    while True:
        b=prg[pos]; pos+=1
        if b==0xff: return start,pos,"".join(out)
        if b==0xfe: out.append("\n")
        elif 0x20<=b<=0x7e: out.append(chr(b))
        else: raise ValueError(f"unexpected byte {b:02x} at PRG {pos-1:06x}")

def main():
    ap=argparse.ArgumentParser(); ap.add_argument("rom"); ap.add_argument("-o","--out",default="remake/generated/room_descriptions.json"); a=ap.parse_args()
    prg=parse(pathlib.Path(a.rom).read_bytes()).prg
    pos=START; records=[]; room=0
    while pos<len(prg):
        triple={}
        try:
            for lang in LANGS:
                start,pos,text=read_text(prg,pos)
                triple[lang]={"prg_offset":start,"text":text.strip()}
        except ValueError:
            break
        triple["room_text_id"]=room; records.append(triple); room+=1
    pathlib.Path(a.out).write_text(json.dumps(records,ensure_ascii=False,indent=2),encoding="utf-8")
    print(f"decoded {len(records)} trilingual room descriptions")
if __name__=="__main__": main()
