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

def parse_chr_upload_list(prg,ptr):
    rows=[]; p=ptr; target_half_raw=1; marker_count=0
    for _ in range(64):
        first=bb(prg,1,p)
        if first==0:
            return rows,marker_count
        if first==0xFF:
            marker_count+=1
            target_half_raw=(target_half_raw-1)&0xff
            p+=1
            first=bb(prg,1,p)
        count=bb(prg,1,p+1)
        dest=bb(prg,1,p+2)
        src=bb(prg,1,p+3)|(bb(prg,1,p+4)<<8)
        assert first in range(0,15)
        assert 0x8000<=src<=0xBFFF
        rows.append({
            'source_bank':first,'count_raw':count,'destination_tile':dest,
            'source_cpu':src,'target_half_raw':target_half_raw&1,
            'decoder':'FA58' if count==0 else 'DE84',
        })
        p+=5
    raise AssertionError(f'unterminated CHR upload list {ptr:04X}')

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

    # C8C2 selects bank 2 for the map stream; C96C indexes 17-byte metatile definitions.
    assert bs(prg,FIXED_BANK,0xC938,12)==bytes.fromhex('a90085378528a9028d0cc020')
    assert bs(prg,FIXED_BANK,0xC96C,31).startswith(bytes.fromhex('a437b9f503d004a000b122a000841d851c0a261d0a261d0a261d0a261d18'))
    assert bytes.fromhex('651c851c9002e61d18655e851c') in bs(prg,FIXED_BANK,0xC96C,55)
    assert bytes.fromhex('a51b205cd5') in bs(prg,FIXED_BANK,0xC990,32)
    assert bs(prg,FIXED_BANK,0xD55C,5)==bytes.fromhex('aa9d0ac060')

    counts=star_counts(prg)
    assert sum(counts.values())==210
    assert counts[16]==0
    assert all(counts[k]>0 for k in range(LOCATION_COUNT) if k!=16)

    locations=[]; normal_edges=[]; special_dests=set(); all_transition_ptrs=set()
    for key,ptr in enumerate(ptrs):
        d=bs(prg,1,ptr,DESCRIPTOR_SIZE)
        width=d[0]; metatile_base=w16(d,3); metatile_bank=d[9]
        left=w16(d,5); right=w16(d,7); map_ptr=w16(d,20)
        chr_upload_list=w16(d,14)
        text_stream=w16(d,16)
        routine_list=w16(d,18)
        chr_uploads,chr_marker_count=parse_chr_upload_list(prg,chr_upload_list)
        assert 1<=width<=96
        assert 0<=metatile_bank<=14
        assert 0x8000<=metatile_base<=0xBFFF
        assert 0xC000<=left<=0xFFFF and 0xC000<=right<=0xFFFF
        assert 0x8000<=map_ptr<=0xBFFF
        grid=list(bs(prg,2,map_ptr,width*6))
        assert len(grid)==width*6
        used=sorted(set(grid))
        assert metatile_base + max(used)*17 + 16 <= 0xBFFF
        metatiles={}
        for cell in used:
            rec=bs(prg,metatile_bank,metatile_base+cell*17,17)
            assert len(rec)==17
            tiles=list(rec[:16])
            metatiles[str(cell)]={
                'tiles_4x4':[tiles[0:4],tiles[4:8],tiles[8:12],tiles[12:16]],
                'raw17':rec[16],
                'raw_hex':rec.hex(),
            }
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
            'world_origin_raw':w16(d,1),
            'metatile_base_cpu':metatile_base,'metatile_bank':metatile_bank,
            'left_transition_cpu':left,'right_transition_cpu':right,
            'raw10_11':w16(d,10),'raw12_13':w16(d,12),
            'chr_upload_list_cpu':chr_upload_list,
            'chr_upload_marker_count':chr_marker_count,
            'chr_uploads':chr_uploads,
            'text_stream_cpu':text_stream,
            'routine_list_cpu':routine_list,
            'map_stream_bank':2,'map_stream_cpu':map_ptr,'map_rows':6,
            'map_cell_count':len(grid),'used_cell_ids':used,'map_cells':grid,
            'metatiles':metatiles,'main_star_count':counts[key],
            'left':lrows,'right':rrows,
        })

    assert len(locations)==50
    assert min(x['width_columns_32px'] for x in locations)==8
    assert max(x['width_columns_32px'] for x in locations)==96
    assert len(all_transition_ptrs)==83
    assert len(normal_edges)==67
    assert special_dests=={50,51,122,123,124,125,126,127,255}
    assert sum(x['main_star_count'] for x in locations)==210
    assert sum(x['map_cell_count'] for x in locations)==12018
    assert all(x['map_rows']==6 and x['map_cell_count']==x['width_columns_32px']*6 for x in locations)
    assert all(x['map_stream_bank']==2 for x in locations)
    assert set(x['metatile_bank'] for x in locations)=={3,4,5,9}
    assert all(0x8000<=x['chr_upload_list_cpu']<=0xBFFF for x in locations)
    assert all(0x8000<=x['text_stream_cpu']<=0xBFFF for x in locations)
    assert all(0x8000<=x['routine_list_cpu']<=0xBFFF for x in locations)
    assert sum(len(x['chr_uploads']) for x in locations)==304
    assert sum(x['chr_upload_marker_count'] for x in locations)==49
    assert [x['key'] for x in locations if not x['chr_uploads']]==[16]
    assert all(x['chr_upload_marker_count']==1 for x in locations if x['key']!=16)
    all_uploads=[u for x in locations for u in x['chr_uploads']]
    assert sorted(set(u['source_bank'] for u in all_uploads))==[1,2,3,4,6,7,8,9,10,13]
    assert sum(1 for u in all_uploads if u['count_raw']==0)==61
    assert sum(1 for u in all_uploads if u['target_half_raw']==1)==84
    assert sum(1 for u in all_uploads if u['target_half_raw']==0)==220
    assert bs(prg,FIXED_BANK,0xC6C4,0x75).find(bytes.fromhex('b1bd'))>=0
    assert bs(prg,FIXED_BANK,0xC6C4,0x75).find(bytes.fromhex('2084de'))>=0
    assert bs(prg,FIXED_BANK,0xF08B,0x20).startswith(bytes.fromhex('a9068d10c0adbd03'))
    assert bytes.fromhex('2070db') in bs(prg,FIXED_BANK,0xF090,0x20)
    assert bs(prg,FIXED_BANK,0xF439,0x36).find(bytes.fromhex('b1cb'))>=0
    assert bs(prg,FIXED_BANK,0xF439,0x36).find(bytes.fromhex('6c1c00'))>=0
    assert bs(prg,FIXED_BANK,0xC96C,0x8D).find(bytes.fromhex('a510290318651c'))>=0
    assert bs(prg,FIXED_BANK,0xC96C,0x8D).find(bytes.fromhex('a000b11c9d0001a004b11c9d0101a008b11c9d0201a00cb11c9d0301'))>=0
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
        'metatile_format':{
            'bytes_per_cell_definition':17,
            'tile_matrix':'first 16 bytes form a 4x4 8x8-tile matrix; C96C selects one column using fine X and offsets 0,4,8,12',
            'raw17':'separate byte consumed by C96C; semantic name intentionally withheld until its PPU/runtime role is proven',
        },
        'chr_upload_summary':{
            'entry_count':304,'single_switch_marker_locations':49,'empty_location_keys':[16],
            'count_zero_fa58_entries':61,'de84_entries':243,
            'target_half_raw_counts':{'1':84,'0':220},
        },
        'descriptor_fields_proven':{
            'bytes_14_15':'CHR upload-list pointer consumed by C6C7 and DE84',
            'bytes_16_17':'bank-6 text stream pointer consumed by F090 and DB70',
            'bytes_18_19':'bank-1 routine-dispatch list consumed by F439',
        },
        'locations':locations,'normal_edges':normal_edges,
    }
    out=Path(a.out); out.mkdir(parents=True,exist_ok=True)
    (out/'location_manifest.json').write_text(json.dumps(result,indent=2),encoding='utf-8')
    print(json.dumps({'locations':50,'normal_edges':len(normal_edges),
        'transition_tables':len(all_transition_ptrs),'special':sorted(special_dests),
        'stars':sum(counts.values())},indent=2))

if __name__=='__main__': main()
