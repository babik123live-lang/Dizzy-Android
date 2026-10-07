#!/usr/bin/env python3
import argparse, hashlib, json
from pathlib import Path

EXPECTED_SHA256='bb6d2b7f6d2eeb216be0c428c1aa45e21a213565b60214dd8d130b8c5eb0cfb1'
BANK_SIZE=0x4000

def load(path):
    raw=Path(path).read_bytes()
    if raw[:4]!=b'NES\x1a': raise SystemExit('not iNES')
    if hashlib.sha256(raw).hexdigest()!=EXPECTED_SHA256: raise SystemExit('unexpected ROM')
    return raw,raw[16:16+raw[4]*BANK_SIZE]

def b0(prg,cpu,n):
    p=cpu-0x8000
    return bytes(prg[p:p+n])

def fixed(prg,cpu,n):
    p=15*BANK_SIZE+(cpu-0xC000)
    return bytes(prg[p:p+n])

def signed(raw): return raw if raw<128 else raw-256
def vdelta(raw):
    v=signed(raw)
    return v//4 if v<0 else v//4

def main():
    ap=argparse.ArgumentParser(); ap.add_argument('rom')
    ap.add_argument('-o','--out',default='remake/generated'); a=ap.parse_args()
    raw,prg=load(a.rom)

    # Input-derived direction and jump latch.
    assert b0(prg,0x8601,0x2D).startswith(bytes.fromhex(
        'a50d2902f00886b6a9ff85d0d00ca50d2901f01086b7a90185d0'))
    assert bytes.fromhex('a50d2980f00286b8') in b0(prg,0x8625,8)

    # Vertical velocity: INC $AB, signed branch, /4 pixel conversion.
    assert b0(prg,0x82F1,0x2B).startswith(bytes.fromhex(
        'e6aba5ab10034c57854ca285a5ab1007386a386a4c17834a4a'))
    assert bytes.fromhex('a6a5e003b007a6ade008b0014a') in b0(prg,0x830A,16)
    assert bytes.fromhex('a9f086ab') in b0(prg,0x8286,12)
    assert bytes.fromhex('a2f886ab') in b0(prg,0x8265,16)

    # Horizontal acceleration $CF, fractional accumulator $D2 and table $870F.
    assert b0(prg,0x86B7,0x58).find(bytes.fromhex('a5cfc930f007e6cf'))>=0
    assert b0(prg,0x86B7,0x58).find(bytes.fromhex('46cf'))>=0
    table=list(b0(prg,0x870F,0x31))
    expected=[0,0,0]+list(range(5,230,5))+[0]
    assert table==expected and len(table)==49
    assert b0(prg,0x86E8,0x22).find(bytes.fromhex('bd0f871865d285d298659c859c'))>=0
    assert b0(prg,0x86FA,0x15).find(bytes.fromhex('a5d238fd0f8785d2a59ce532e532859c'))>=0

    # Player point-collision probes used by bank-0 movement.
    probe_sequences={
        'feet_minus4':bytes.fromhex('a4a388a5a21869fcaa200ddf'),
        'feet_plus4':bytes.fromhex('a4a388a5a2186904aa200ddf'),
        'upper_minus8':bytes.fromhex('a5a21869f8aaa5a31869f4a8200ddf'),
        'head_minus4':bytes.fromhex('a5a31869e6a8a5a21869fcaa200ddf'),
        'head_plus4':bytes.fromhex('a5a31869e6a8a5a2186904aa200ddf'),
    }
    region=b0(prg,0x8342,0x2A0)
    for name,seq in probe_sequences.items():
        assert seq in region,name

    # Collision bitset consumer and mask order.
    assert fixed(prg,0xDFF1,15)==bytes.fromhex('b900c2a8b1bba0008c0ac03d00e0')
    assert fixed(prg,0xE000,8)==bytes.fromhex('8040201008040201')

    v=0xF0; rise=0
    for _ in range(16):
        v=(v+1)&0xFF
        rise+=vdelta(v)
    assert v==0 and rise==-36

    result={
        'rom_sha256':hashlib.sha256(raw).hexdigest(),
        'normal_jump_velocity_raw':0xF0,
        'special_jump_velocity_raw':0xF8,
        'base_no_collision_rise_px':36,
        'horizontal_accel_max':0x30,
        'horizontal_fraction_table':table,
        'collision_probe_offsets':[
            {'x':-4,'y':-1},{'x':4,'y':-1},
            {'x':-8,'y':-12},{'x':8,'y':-12},
            {'x':-4,'y':-26},{'x':4,'y':-26},
            {'x':-4,'y':0},{'x':4,'y':0},
        ],
    }
    out=Path(a.out); out.mkdir(parents=True,exist_ok=True)
    (out/'player_motion.json').write_text(json.dumps(result,indent=2),encoding='utf-8')
    print(json.dumps(result,indent=2))

if __name__=='__main__': main()
