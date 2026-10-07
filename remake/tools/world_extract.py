#!/usr/bin/env python3
import argparse, hashlib, json
from pathlib import Path

EXPECTED_SHA256='bb6d2b7f6d2eeb216be0c428c1aa45e21a213565b60214dd8d130b8c5eb0cfb1'
BANK_SIZE=0x4000
FIXED_BANK=15
POINTER_TABLE_CPU=0x8003
LOCATION_COUNT=50
DESCRIPTOR_SIZE=22
TRANSITION_ROUTINE_CPU=0xC748

def load_rom(path):
    raw=Path(path).read_bytes()
    if raw[:4] != b'NES\x1a': raise SystemExit('not iNES')
    prg_size=raw[4]*BANK_SIZE
    prg=raw[16:16+prg_size]
    if len(prg)!=prg_size: raise SystemExit('truncated PRG')
    return raw,prg

def off(bank,cpu):
    if bank==FIXED_BANK:
        if not 0xC000<=cpu<=0xFFFF: raise ValueError((bank,hex(cpu)))
        return bank*BANK_SIZE+(cpu-0xC000)
    if not 0x8000<=cpu<=0xBFFF: raise ValueError((bank,hex(cpu)))
    return bank*BANK_SIZE+(cpu-0x8000)

def bb(prg,bank,cpu): return prg[off(bank,cpu)]
def bs(prg,bank,cpu,n):
    p=off(bank,cpu); return bytes(prg[p:p+n])
def w16(b,i): return b[i] | (b[i+1]<<8)

def transition_table(prg,ptr):
    rows=[]; p=ptr
    for _ in range(64):
        raw=bs(prg,FIXED_BANK,p,3)
        threshold=w16(raw,0); dest=raw[2]
        rows.append({'threshold_x':threshold,'destination_raw':dest,'cpu_address':p})
        p+=3
        if threshold==0xFFFF: return rows
    raise AssertionError(f'unterminated transition table {ptr:04X}')

def star_counts(prg):
    counts={k:0 for k in range(LOCATION_COUNT)}
    for i in range(210):
        k=bb(prg,13,0xB312+i)
        if k < LOCATION_COUNT: counts[k]+=1
    return counts

def main():
    ap=argparse.ArgumentParser(); ap.add_argument('rom')
    ap.add_argument('-o','--out',default='remake/generated'); a=ap.parse_args()
    raw,prg=load_rom(a.rom); sha=hashlib.sha256(raw).hexdigest()
    assert sha==EXPECTED_SHA256
    assert len(prg)==16*BANK_SIZE
    assert raw[5]==0
    assert ((raw[6]>>4)|(raw[7]&0xF0))==71

    assert bs(prg,FIXED_BANK,0xC500,19)==bytes.fromhex('a9018d0bc0a5930aaabd03808550bd04808551')
    ptrs=[]
    for key in range(LOCATION_COUNT):
        p=POINTER_TABLE_CPU+key*2
        ptr=bb(prg,1,p)|(bb(prg,1,p+1)<<8)
        assert 0x8000<=ptr<=0xBFFF
        ptrs.append(ptr)
    assert len(set(ptrs))==LOCATION_COUNT
    p50=bb(prg,1,POINTER_TABLE_CPU+100)|(bb(prg,1,POINTER_TABLE_CPU+101)<<8)
    assert not (0x8000<=p50<=0xBFFF)
    assert bs(prg,0,0x848F,30).startswith(bytes.fromhex('a593c932'))
    assert bytes.fromhex('a9188593') in bs(prg,0,0x848F,30)
    assert bs(prg,0,0x84AD,8)==bytes.fromhex('a593c933d00320d0')
    assert bs(prg,0,0x81F5,10)==bytes.fromhex('a9018dc603a92e859360')

    tr=bs(prg,FIXED_BANK,TRANSITION_ROUTINE_CPU,41)
    assert tr[:18]==bytes.fromhex('a000b11c851ec8b11c851fc8a51fc59dd004')
    assert bytes.fromhex('a51ec59cb004c84c4ac7') in tr
    assert bytes.fromhex('b11cc92dd006a493c011d00060') in tr
    assert bs(prg,0,0x838F,10)==bytes.fromhex('a6a3e0beb03fe01cb055')
    assert bs(prg,0,0x83AA,11)==bytes.fromhex('addd03851cadde03851d20')
    assert bs(prg,0,0x83D8,11)==bytes.fromhex('addf03851cade003851d20')

    counts=star_counts(prg)
    assert sum(counts.values())==210
    assert counts[16]==0
    assert all(counts[k]>0 for k in range(LOCATION_COUNT) if k!=16)

    locations=[]; normal_edges=[]; special_dests=set(); all_transition_ptrs=set()
    for key,ptr in enumerate(ptrs):
        d=bs(prg,1,ptr,DESCRIPTOR_SIZE)
        width=d[0]; left=w16(d,5); right=w16(d,7); map_ptr=w16(d,20)
        assert 1<=width<=96
        assert 0xC000<=left<=0xFFFF and 0xC000<=right<=0xFFFF
        assert 0x8000<=map_ptr<=0xBFFF
        lrows=transition_table(prg,left); rrows=transition_table(prg,right)
        assert lrows[-1]['threshold_x']==0xFFFF
        assert rrows[-1]['threshold_x']==0xFFFF
        assert [x['threshold_x'] for x in lrows]==sorted(x['threshold_x'] for x in lrows)
        assert [x['threshold_x'] for x in rrows]==sorted(x['threshold_x'] for x in rrows)
        for side,rows in [('left',lrows),('right',rrows)]:
            for row in rows:
                dest=row['destination_raw']
                if dest<LOCATION_COUNT:
                    normal_edges.append({'from':key,'side':side,'threshold_x':row['threshold_x'],'to':dest})
                else: special_dests.add(dest)
        all_transition_ptrs|={left,right}
        locations.append({
            'key':key,'descriptor_cpu':ptr,'width_columns_32px':width,
            'world_origin_raw':w16(d,1),'raw_3_4':w16(d,3),
            'left_transition_cpu':left,'right_transition_cpu':right,
            'raw9':d[9],'raw10_11':w16(d,10),'raw12_13':w16(d,12),
            'raw14_15':w16(d,14),'raw16_17':w16(d,16),'raw18_19':w16(d,18),
            'map_stream_cpu':map_ptr,'main_star_count':counts[key],
            'left':lrows,'right':rrows,
        })

    assert len(locations)==50
    assert min(x['width_columns_32px'] for x in locations)==8
    assert max(x['width_columns_32px'] for x in locations)==96
    assert len(all_transition_ptrs)==83
    assert len(normal_edges)==67
    assert special_dests=={50,51,122,123,124,125,126,127,255}
    assert sum(x['main_star_count'] for x in locations)==210
    assert [x['key'] for x in locations]==list(range(50))
    assert locations[0]['descriptor_cpu']==0xA882
    assert locations[16]['descriptor_cpu']==0xB615
    assert locations[49]['descriptor_cpu']==0xB62C
    assert locations[0]['width_columns_32px']==15
    assert locations[18]['width_columns_32px']==96
    assert locations[31]['width_columns_32px']==96
    assert locations[49]['width_columns_32px']==16
    assert locations[8]['right'][-1]['destination_raw']==9
    assert locations[9]['left'][-1]['destination_raw']==8
    assert locations[15]['right'][0]=={'threshold_x':500,'destination_raw':19,'cpu_address':0xC7A1}
    assert locations[24]['left'][1]['destination_raw']==33
    assert locations[33]['right'][-1]['destination_raw']==50
    assert locations[18]['left'][-1]['destination_raw']==51
    assert locations[45]['left'][-1]['destination_raw']==17
    assert locations[49]['left'][-1]['destination_raw']==22

    result={
        'rom_sha256':sha,'location_count':50,
        'descriptor_pointer_table':{'bank':1,'cpu':'0x8003','index':'RAM $93 * 2'},
        'world_x_ram':'0x009C-0x009D','screen_x_ram':'0x00A3',
        'normal_transition_edge_count':len(normal_edges),
        'unique_transition_table_count':len(all_transition_ptrs),
        'special_destination_values':sorted(special_dests),
        'locations':locations,'normal_edges':normal_edges,
    }
    out=Path(a.out); out.mkdir(parents=True,exist_ok=True)
    (out/'location_manifest.json').write_text(json.dumps(result,indent=2),encoding='utf-8')
    print(json.dumps({'locations':50,'normal_edges':len(normal_edges),
        'transition_tables':len(all_transition_ptrs),'special':sorted(special_dests),
        'stars':sum(counts.values())},indent=2))

if __name__=='__main__': main()
