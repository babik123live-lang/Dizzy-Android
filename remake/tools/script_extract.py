#!/usr/bin/env python3
import argparse, hashlib, json
from collections import Counter
from pathlib import Path

EXPECTED_SHA256='bb6d2b7f6d2eeb216be0c428c1aa45e21a213565b60214dd8d130b8c5eb0cfb1'
BANK_SIZE=0x4000
LOCATION_COUNT=50
POINTER_TABLE_CPU=0x8003
ARG_COUNTS={0xF498:3,0xF46F:1,0xF492:1,0xF4C1:5}

def load_rom(path):
    raw=Path(path).read_bytes()
    if raw[:4] != b'NES\x1a': raise SystemExit('not iNES')
    prg=raw[16:16+raw[4]*BANK_SIZE]
    if hashlib.sha256(raw).hexdigest()!=EXPECTED_SHA256: raise SystemExit('unexpected ROM')
    return raw,prg

def off(bank,cpu):
    if bank==15:
        if not 0xC000<=cpu<=0xFFFF: raise ValueError((bank,hex(cpu)))
        return bank*BANK_SIZE+(cpu-0xC000)
    if not 0x8000<=cpu<=0xBFFF: raise ValueError((bank,hex(cpu)))
    return bank*BANK_SIZE+(cpu-0x8000)

def bs(prg,bank,cpu,n):
    p=off(bank,cpu)
    return bytes(prg[p:p+n])

def w(prg,bank,cpu):
    d=bs(prg,bank,cpu,2)
    return d[0] | (d[1]<<8)

def descriptor_ptr(prg,key):
    return w(prg,1,POINTER_TABLE_CPU+key*2)

def script_ptr(prg,key):
    d=bs(prg,1,descriptor_ptr(prg,key),22)
    return d[18] | (d[19]<<8)

def parse_script(prg,ptr):
    p=ptr; ops=[]
    for _ in range(128):
        lo,hi=bs(prg,1,p,2)
        if hi==0:
            return ops,p+2
        handler=lo | (hi<<8)
        if not 0x8000<=handler<=0xFFFF:
            raise AssertionError((hex(ptr),hex(p),hex(handler)))
        p+=2
        argc=ARG_COUNTS.get(handler,0)
        args=list(bs(prg,1,p,argc))
        p+=argc
        ops.append({'handler_cpu':handler,'args':args})
    raise AssertionError(f'unterminated script {ptr:04X}')

def main():
    ap=argparse.ArgumentParser(); ap.add_argument('rom')
    ap.add_argument('-o','--out',default='remake/generated'); a=ap.parse_args()
    raw,prg=load_rom(a.rom)

    # Dispatcher proof: F439 reads 16-bit handlers from bank 1, restores bank 0,
    # and JMPs through $001C. High byte zero terminates the list.
    assert bs(prg,15,0xF439,0x36).startswith(bytes.fromhex(
        'a5c985cba5ca85cca000a9018d0bc0b1cb851ce6cbd002e6ccb1cbf013'))
    assert bytes.fromhex('a9008d0ac06c1c00') in bs(prg,15,0xF439,0x36)
    assert bytes.fromhex('f013') in bs(prg,15,0xF452,4)

    # Known byte-consuming handlers.
    assert bs(prg,15,0xF498,0x22).find(bytes.fromhex('6903'))>=0
    assert bs(prg,15,0xF475,0x10).find(bytes.fromhex('e6cb'))>=0
    assert bs(prg,1,0xBB38,0x18).find(bytes.fromhex('c005'))>=0
    assert bs(prg,1,0xBB44,0x10).find(bytes.fromhex('6905'))>=0

    scripts=[]; counts=Counter()
    f498_placements=[]; f46f_args=[]; f492_args=[]; f4c1_ranges=[]
    for key in range(LOCATION_COUNT):
        ptr=script_ptr(prg,key)
        ops,end=parse_script(prg,ptr)
        for op_index,op in enumerate(ops):
            h=op['handler_cpu']; args=op['args']; counts[h]+=1
            if h==0xF498:
                f498_placements.append({'location_key':key,'op_index':op_index,
                    'world_x':args[0]|(args[1]<<8),'y':args[2]})
            elif h==0xF46F:
                f46f_args.append({'location_key':key,'op_index':op_index,'raw':args[0]})
            elif h==0xF492:
                f492_args.append({'location_key':key,'op_index':op_index,'raw':args[0]})
            elif h==0xF4C1:
                f4c1_ranges.append({'location_key':key,'op_index':op_index,
                    'x_start':args[0]|(args[1]<<8),'x_end':args[2]|(args[3]<<8),
                    'raw_parameter':args[4]})
        scripts.append({'location_key':key,'script_cpu':ptr,'op_count':len(ops),'ops':ops,'end_cpu':end})

    assert len(scripts)==50
    assert sum(x['op_count'] for x in scripts)==284
    assert len(counts)==63
    assert scripts[16]['op_count']==0
    assert counts[0xF498]==100
    assert counts[0x896D]==49
    assert counts[0xF46F]==23
    assert counts[0xB882]==9
    assert counts[0xBB6D]==9
    assert counts[0x9E13]==8
    assert counts[0xFBEF]==7
    assert counts[0xBF9B]==7
    assert counts[0xF4C1]==4
    assert counts[0xF492]==3
    assert len(f498_placements)==100
    assert min(x['world_x'] for x in f498_placements)==24
    assert max(x['world_x'] for x in f498_placements)==2504
    assert min(x['y'] for x in f498_placements)==20
    assert max(x['y'] for x in f498_placements)==152
    assert len(f46f_args)==23 and sorted(set(x['raw'] for x in f46f_args))==[3,10,46,86,208,214]
    assert len(f492_args)==3 and {x['raw'] for x in f492_args}=={115}
    assert len(f4c1_ranges)==4
    assert [(x['x_start'],x['x_end'],x['raw_parameter']) for x in f4c1_ranges]==[
        (100,500,176),(804,928,0),(0,200,144),(0,184,176)]

    # F498's three bytes are proven by F49D-F4A9 and $87EB:
    # 16-bit world X is camera-relative, byte 3 supplies Y, and two adjacent
    # animation tiles are emitted through D87F.
    assert bs(prg,15,0xF49D,0x1E).startswith(bytes.fromhex('b1cb8520c8b1cb8521c8b1cb8522'))
    assert bs(prg,0,0x87EB,0x3A).find(bytes.fromhex('a52038edbb038524aa'))>=0
    assert bs(prg,0,0x87EB,0x3A).find(bytes.fromhex('a5221869f8a8'))>=0
    assert bs(prg,0,0x87EB,0x3A).count(bytes.fromhex('207fd8'))==2

    result={
        'rom_sha256':hashlib.sha256(raw).hexdigest(),
        'location_count':50,
        'total_ops':284,
        'unique_handlers':63,
        'known_argument_handlers':{hex(k):v for k,v in ARG_COUNTS.items()},
        'handler_counts':{hex(k):v for k,v in sorted(counts.items())},
        'f498_two_tile_placements':f498_placements,
        'f46f_single_args':f46f_args,
        'f492_single_args':f492_args,
        'f4c1_world_x_ranges':f4c1_ranges,
        'scripts':scripts,
    }
    out=Path(a.out); out.mkdir(parents=True,exist_ok=True)
    (out/'location_scripts.json').write_text(json.dumps(result,indent=2),encoding='utf-8')
    print(json.dumps({'locations':50,'ops':284,'handlers':63,'empty':[x['location_key'] for x in scripts if not x['ops']]},indent=2))

if __name__=='__main__': main()
