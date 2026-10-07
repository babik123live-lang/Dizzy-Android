#!/usr/bin/env python3
from dataclasses import dataclass
@dataclass(frozen=True)
class INes:
    prg:bytes; chr:bytes; mapper:int; mirroring:str
def parse(raw:bytes)->INes:
    if raw[:4]!=b"NES\x1a": raise ValueError("not iNES")
    trainer=512 if raw[6]&4 else 0
    p=16+trainer; ps=raw[4]*16384; cs=raw[5]*8192
    mapper=(raw[6]>>4)|(raw[7]&0xf0)
    mirroring="four-screen" if raw[6]&8 else ("vertical" if raw[6]&1 else "horizontal")
    return INes(raw[p:p+ps],raw[p+ps:p+ps+cs],mapper,mirroring)
