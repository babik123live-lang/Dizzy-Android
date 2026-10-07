#!/usr/bin/env python3
import argparse, hashlib, json
from pathlib import Path

EXPECTED_SHA256='bb6d2b7f6d2eeb216be0c428c1aa45e21a213565b60214dd8d130b8c5eb0cfb1'
FIXED_BANK=15
RESOURCE_TABLE_CPU=0xDD55
RESOURCE_COUNT=28
MAIN_COUNT=210
ANIM_CPU=0xFCA2

def load_rom(path):
    raw=Path(path).read_bytes()
    if raw[:4] != b'NES\x1a': raise SystemExit('not iNES')
    prg_size=raw[4]*16384
    prg=raw[16:16+prg_size]
    if len(prg)!=prg_size: raise SystemExit('truncated PRG')
    return raw,prg

def bank_byte(prg,bank,addr):
    if bank==15:
        if not 0xC000<=addr<=0xFFFF: raise ValueError(hex(addr))
        off=15*0x4000+(addr-0xC000)
    else:
        if not 0x8000<=addr<=0xBFFF: raise ValueError((bank,hex(addr)))
        off=bank*0x4000+(addr-0x8000)
    return prg[off]

def bank_bytes(prg,bank,addr,n):
    return bytes(bank_byte(prg,bank,addr+i) for i in range(n))

def resource_entry(prg,rid):
    b=bank_bytes(prg,FIXED_BANK,RESOURCE_TABLE_CPU+5*rid,5)
    return {'id':rid,'source_cpu':b[0]|(b[1]<<8),'destination_tile':b[2],
            'tile_count':b[3],'config_raw':b[4],'source_bank':b[4]&0x0F,
            'pattern_table':(b[4]>>7)&1}

def decode_de84_block(prg,bank,p):
    ctrl=bank_byte(prg,bank,p); repeat=ctrl&0x0F; literal=(ctrl>>4)+1
    x=16; p=(p+1)&0xFFFF; out=[]
    while True:
        a=bank_byte(prg,bank,p); out.append(a); literal=(literal-1)&0xFF
        if literal!=0:
            x=(x-1)&0xFF
            if x: p=(p+1)&0xFFFF; continue
            p=(p+1)&0xFFFF; break
        x=(x-1)&0xFF
        if not x: p=(p+1)&0xFFFF; break
        out.append(a); repeat=(repeat-1)&0xFF
        while repeat!=0:
            x=(x-1)&0xFF
            if not x: return bytes(out),(p+1)&0xFFFF,ctrl
            out.append(a); repeat=(repeat-1)&0xFF
        x=(x-1)&0xFF
        if x: p=(p+1)&0xFFFF; continue
        p=(p+1)&0xFFFF; break
    if len(out)!=16: raise AssertionError((hex(ctrl),len(out)))
    return bytes(out),p,ctrl

def decode_resource(prg,e):
    p=e['source_cpu']; out=bytearray(); controls=[]
    for _ in range(e['tile_count']):
        block,p,ctrl=decode_de84_block(prg,e['source_bank'],p)
        out.extend(block); controls.append(ctrl)
    return bytes(out),p,controls

def pixels_2bpp(tile):
    if len(tile)!=16: raise ValueError(len(tile))
    return [[(((tile[y+8]>>(7-x))&1)<<1)|((tile[y]>>(7-x))&1)
             for x in range(8)] for y in range(8)]

def main():
    ap=argparse.ArgumentParser(); ap.add_argument('rom')
    ap.add_argument('-o','--out',default='remake/generated'); a=ap.parse_args()
    raw,prg=load_rom(a.rom); sha=hashlib.sha256(raw).hexdigest()
    if sha!=EXPECTED_SHA256: raise SystemExit(f'unexpected ROM sha256 {sha}')

    resources=[resource_entry(prg,i) for i in range(RESOURCE_COUNT)]
    star_res=resources[23]
    assert star_res['destination_tile']==0x4F and star_res['tile_count']==4
    assert star_res['pattern_table']==0
    decoded,end,controls=decode_resource(prg,star_res)
    assert len(decoded)==64 and controls==[0xF0]*4 and end==0xB380

    anim=list(bank_bytes(prg,FIXED_BANK,ANIM_CPU,7))
    assert anim==[0x4F,0x50,0x51,0x52,0x51,0x50,0x4F]

    stars=[]
    for i in range(MAIN_COUNT):
        key=bank_byte(prg,13,0xB312+i)
        wx=bank_byte(prg,13,0xB3E5+i)|(bank_byte(prg,13,0xB4B8+i)<<8)
        y=bank_byte(prg,13,0xB58B+i)
        stars.append({'id':i,'layout':'horizontal','group_raw':key,
                      'world_axis':wx,'fixed_axis':y,'state_bit':i})
    assert bank_byte(prg,13,0xB312+MAIN_COUNT)==0xFF

    for bank,addr,count,start,label in [(10,0x8F43,10,210,'vertical_a'),
        (13,0x8CFE,20,220,'vertical_b'),(14,0x9260,10,240,'vertical_c')]:
        for n in range(count):
            lo=bank_byte(prg,bank,addr+3*n); hi=bank_byte(prg,bank,addr+3*n+1)
            x=bank_byte(prg,bank,addr+3*n+2)
            stars.append({'id':start+n,'layout':label,'world_axis':lo|(hi<<8),
                          'fixed_axis':x,'state_bit':start+n})

    assert len(stars)==250 and [s['id'] for s in stars]==list(range(250))
    state_map=list(bank_bytes(prg,FIXED_BANK,0xC200,250))
    assert all(state_map[i]==i//8 for i in range(250))
    assert bytes([0xA9,0xFA,0x8D,0x72,0x07]) in bank_bytes(prg,FIXED_BANK,0xF8D7,15)

    tiles=[]
    for i,tile_id in enumerate(range(0x4F,0x53)):
        tb=decoded[i*16:(i+1)*16]
        tiles.append({'tile':tile_id,'raw_hex':tb.hex(),'pixels':pixels_2bpp(tb)})

    result={'rom_sha256':sha,'proof':{'total_stars':250,'main_horizontal_count':210,
        'vertical_counts':[10,20,10],'counter_ram':'0x0772','counter_initial':250,
        'packed_state_ram':'0x0751-0x0770','animation_tiles':anim,
        'graphics_resource_id':23},'graphics_resource':star_res,'tiles':tiles,'stars':stars}
    out=Path(a.out); out.mkdir(parents=True,exist_ok=True)
    (out/'star_manifest.json').write_text(json.dumps(result,indent=2),encoding='utf-8')
    (out/'graphics_resources.json').write_text(json.dumps(resources,indent=2),encoding='utf-8')
    print(json.dumps({'stars':len(stars),'resource':star_res,'resource_end_cpu':hex(end),
                      'animation':anim},indent=2))

if __name__=='__main__': main()
