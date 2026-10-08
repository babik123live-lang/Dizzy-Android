#!/usr/bin/env python3
import argparse, hashlib, json
from collections import Counter
from pathlib import Path

EXPECTED_SHA256='bb6d2b7f6d2eeb216be0c428c1aa45e21a213565b60214dd8d130b8c5eb0cfb1'
BANK_SIZE=0x4000
FIXED_BANK=15
OBJECT_CPU=0xF550
OBJECT_COUNT=64
OBJECT_SIZE=10
BEHAVIOR_TABLE_CPU=0x8C80

def load_rom(path):
    raw=Path(path).read_bytes()
    if raw[:4] != b'NES\x1a': raise SystemExit('not iNES')
    prg=raw[16:16+raw[4]*BANK_SIZE]
    if hashlib.sha256(raw).hexdigest()!=EXPECTED_SHA256:
        raise SystemExit('unexpected ROM')
    return raw,prg

def off(bank,cpu):
    if bank==FIXED_BANK:
        if not 0xC000<=cpu<=0xFFFF: raise ValueError((bank,hex(cpu)))
        return bank*BANK_SIZE+(cpu-0xC000)
    if not 0x8000<=cpu<=0xBFFF: raise ValueError((bank,hex(cpu)))
    return bank*BANK_SIZE+(cpu-0x8000)

def bs(prg,bank,cpu,n):
    p=off(bank,cpu)
    return bytes(prg[p:p+n])

def b(prg,bank,cpu): return bs(prg,bank,cpu,1)[0]
def w16(data,o=0): return data[o] | (data[o+1]<<8)

def decode_de84_block(prg,bank,p):
    ctrl=b(prg,bank,p); repeat=ctrl&0x0F; literal=(ctrl>>4)+1
    x=16; p+=1; out=[]
    while True:
        a=b(prg,bank,p); out.append(a); literal=(literal-1)&0xFF
        if literal:
            x=(x-1)&0xFF
            if x: p+=1; continue
            p+=1; break
        x=(x-1)&0xFF
        if not x: p+=1; break
        out.append(a); repeat=(repeat-1)&0xFF
        while repeat:
            x=(x-1)&0xFF
            if not x: return bytes(out),p+1,ctrl
            out.append(a); repeat=(repeat-1)&0xFF
        x=(x-1)&0xFF
        if x: p+=1; continue
        p+=1; break
    if len(out)!=16: raise AssertionError((hex(p),hex(ctrl),len(out)))
    return bytes(out),p,ctrl

def decode_tiles(prg,bank,ptr,count):
    p=ptr; out=[]; controls=[]
    for _ in range(count):
        tile,p,ctrl=decode_de84_block(prg,bank,p)
        out.append(tile.hex()); controls.append(ctrl)
    return out,p,controls

def read_inventory_record(prg,ptr):
    graphics=w16(bs(prg,9,ptr,2))
    p=ptr+2; chars=[]
    for _ in range(256):
        v=b(prg,9,p); p+=1
        if v==0xFF: break
        if v==0xFE: chars.append('\n')
        elif 32<=v<=126: chars.append(chr(v))
        else: chars.append(f'<{v:02X}>')
    else:
        raise AssertionError(f'unterminated inventory text {ptr:04X}')
    return graphics,''.join(chars),p

def main():
    ap=argparse.ArgumentParser(); ap.add_argument('rom')
    ap.add_argument('-o','--out',default='remake/generated'); a=ap.parse_args()
    raw,prg=load_rom(a.rom)

    # Core loader/dispatcher proofs.
    assert bs(prg,0,0x8C5F,18).startswith(bytes.fromhex('a006b11e0aaabd808c8522bd818c85236c2200'))
    assert bs(prg,15,0xF86C,0x35).startswith(bytes.fromhex('a200861f851e0a261f0a261f18651e'))
    assert bytes.fromhex('186950851e') in bs(prg,15,0xF86C,0x35)
    assert bytes.fromhex('a9098d13c0206aac') in bs(prg,15,0xF0E0,0x18)
    assert bs(prg,9,0xAC6A,10)==bytes.fromhex('a007b11e8524c8b11e8525')
    assert bytes.fromhex('a5241869028560a52569008561') in bs(prg,9,0xAC6A,0x50)
    assert bytes.fromhex('a000b1248526c8b124852760') in bs(prg,9,0xAC90,0x30)
    assert bytes.fromhex('a9038d0dc0') in bs(prg,15,0xF0E8,0x10)
    assert bytes.fromhex('a9108532a626a427') in bs(prg,15,0xF110,0x18)
    assert bytes.fromhex('2067de') in bs(prg,0,0x8927,0x30)

    bt=bs(prg,0,BEHAVIOR_TABLE_CPU,64)
    handlers=[w16(bt,i*2) for i in range(32)]
    expected_handlers=[
        0x8C72,0x8CC3,0x8D35,0xA027,0xA22B,0x8E0C,0x96E4,0x90BA,
        0x9B69,0xA7A3,0x90F9,0x912A,0x94F8,0x95EB,0x9179,0x8E7E,
        0x904B,0x9087,0x9537,0x91C9,0x9417,0x9258,0xAACA,0xABED,
        0x9C80,0x9D29,0xACAE,0xAF73,0xAD66,0xAFF9,0xB06B,0xBAC3]
    assert handlers==expected_handlers

    objects=[]; behavior_counts=Counter(); world_graphics_hashes=Counter()
    inventory_graphics_hashes=Counter()
    for i in range(OBJECT_COUNT):
        r=bs(prg,FIXED_BANK,OBJECT_CPU+i*OBJECT_SIZE,OBJECT_SIZE)
        loc=r[0]; wx=r[1]|(r[2]<<8); y=r[3]; world_gfx=w16(r,4)
        behavior=r[6]; inventory_record=w16(r,7); param=r[9]
        icon_gfx,text,text_end=read_inventory_record(prg,inventory_record)
        world_tiles,world_end,world_ctrl=decode_tiles(prg,3,world_gfx,4)
        icon_header=bs(prg,3,icon_gfx,3)
        icon_tiles,icon_end,icon_ctrl=decode_tiles(prg,3,icon_gfx+3,16)
        wh=hashlib.sha1(bytes.fromhex(''.join(world_tiles))).hexdigest()
        ih=hashlib.sha1(bytes.fromhex(''.join(icon_tiles))).hexdigest()
        world_graphics_hashes[wh]+=1; inventory_graphics_hashes[ih]+=1
        behavior_counts[behavior]+=1
        objects.append({
            'source_index':i,'location_key':loc,'world_x':wx,'sprite_y':y,
            'world_graphics_cpu':world_gfx,'behavior_index':behavior,
            'behavior_handler_cpu':handlers[behavior],
            'inventory_record_cpu':inventory_record,'behavior_parameter':param,
            'inventory_graphics_cpu':icon_gfx,'description_en':text,
            'world_tiles_hex':world_tiles,'world_stream_end_cpu':world_end,
            'world_stream_controls':world_ctrl,
            'inventory_header_hex':icon_header.hex(),
            'inventory_tiles_hex':icon_tiles,'inventory_stream_end_cpu':icon_end,
            'inventory_stream_controls':icon_ctrl,'text_end_cpu':text_end,
        })

    assert b(prg,FIXED_BANK,0xF7D0)==0xFF
    assert len(objects)==64
    assert set(o['behavior_index'] for o in objects)==set(range(32))
    assert sum(behavior_counts.values())==64
    assert behavior_counts[0]==18 and behavior_counts[1]==8 and behavior_counts[3]==5
    assert len(set(o['inventory_record_cpu'] for o in objects))==55
    assert len(set(o['inventory_graphics_cpu'] for o in objects))==46
    assert len(world_graphics_hashes)==46
    assert len(inventory_graphics_hashes)==46
    assert objects[0]['description_en']=='NOTHING'
    assert objects[2]['description_en']=='A MACHINE WRENCH'
    assert objects[9]['description_en']=="DIZZY'S DOOR KEY"
    assert objects[47]['description_en']=='A LARGE GOLD COIN'
    assert objects[63]['description_en']=='A BARREL OF\nPIRATES RUM'
    assert [o['behavior_parameter'] for o in objects if o['behavior_index']==1]==[0,1,2,3,4,5,6,7]
    assert sorted(o['behavior_parameter'] for o in objects if o['behavior_index']==3)==[0,1,2,3,4]

    result={
        'rom_sha256':hashlib.sha256(raw).hexdigest(),
        'object_count':64,'behavior_handler_count':32,
        'behavior_handlers_cpu':[hex(x) for x in handlers],
        'behavior_counts':{str(k):v for k,v in sorted(behavior_counts.items())},
        'unique_inventory_records':len(set(o['inventory_record_cpu'] for o in objects)),
        'unique_world_graphics':len(world_graphics_hashes),
        'unique_inventory_graphics':len(inventory_graphics_hashes),
        'objects':objects,
    }
    out=Path(a.out); out.mkdir(parents=True,exist_ok=True)
    (out/'persistent_objects.json').write_text(json.dumps(result,indent=2),encoding='utf-8')
    print(json.dumps({
        'objects':64,'handlers':32,'unique_inventory_records':result['unique_inventory_records'],
        'unique_world_graphics':result['unique_world_graphics'],
        'unique_inventory_graphics':result['unique_inventory_graphics'],
    },indent=2))

if __name__=='__main__': main()
