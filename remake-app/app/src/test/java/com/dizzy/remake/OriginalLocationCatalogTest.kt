package com.dizzy.remake

import com.dizzy.remake.core.OriginalLocationCatalog
import org.junit.Assert.*
import org.junit.Test

class OriginalLocationCatalogTest {
    @Test fun romDerivedLocationCatalogIsConsistent() {
        val all=OriginalLocationCatalog.all
        assertEquals(50,OriginalLocationCatalog.COUNT)
        assertEquals(50,all.size)
        assertEquals((0 until 50).toList(),all.map{it.key})
        assertEquals(50,all.map{it.descriptorCpu}.toSet().size)
        assertTrue(all.all{it.widthColumns32 in 1..96})
        assertEquals(8,all.minOf{it.widthColumns32})
        assertEquals(96,all.maxOf{it.widthColumns32})
        assertTrue(all.all{it.widthPx==it.widthColumns32*32})
        assertEquals(210,all.sumOf{it.mainStarCount})
        assertEquals(0,all[16].mainStarCount)
        assertTrue(all.filter{it.key!=16}.all{it.mainStarCount>0})
        assertEquals(0xA882,all[0].descriptorCpu)
        assertEquals(0xB615,all[16].descriptorCpu)
        assertEquals(0xB62C,all[49].descriptorCpu)
        assertEquals(15,all[0].widthColumns32)
        assertEquals(96,all[18].widthColumns32)
        assertEquals(96,all[31].widthColumns32)
        assertEquals(16,all[49].widthColumns32)
        assertEquals(0xC771,all[0].leftTransitionCpu)
        assertEquals(0xC774,all[0].rightTransitionCpu)
        assertEquals(0xC777,all[8].rightTransitionCpu)
        assertEquals(0xC77A,all[9].leftTransitionCpu)
        assertEquals(0xC7A1,all[15].rightTransitionCpu)
        assertEquals(0xC888,all[16].leftTransitionCpu)
        assertEquals(0xC888,all[45].leftTransitionCpu)
        assertEquals(0xC8A9,all[49].leftTransitionCpu)
        assertEquals(0x8001,all[0].mapStreamCpu)
        assertEquals(0xAB93,all[16].mapStreamCpu)
        assertEquals(0xAD43,all[49].mapStreamCpu)
        assertEquals(9,all[15].mainStarCount)
        assertEquals(9,all[18].mainStarCount)
        assertEquals(3,all[49].mainStarCount)
    }
}
