package com.dizzy.remake

import com.dizzy.remake.core.OriginalLocationCatalog
import com.dizzy.remake.core.OriginalTransitionCatalog
import org.junit.Assert.*
import org.junit.Test

class OriginalTransitionCatalogTest {
    @Test fun exactTransitionTablesResolveLikeTheRom() {
        val t=OriginalTransitionCatalog.tables
        assertEquals(83,t.size)
        assertEquals(104,t.values.sumOf{it.size})
        assertTrue(t.values.all{it.isNotEmpty()})
        assertTrue(t.values.all{it.last().thresholdX==0xFFFF})
        assertTrue(t.values.all{rows->rows.zipWithNext().all{(a,b)->a.thresholdX<=b.thresholdX}})

        assertEquals(255,OriginalTransitionCatalog.resolve(0xC771,0))
        assertEquals(122,OriginalTransitionCatalog.resolve(0xC774,0))
        assertEquals(9,OriginalTransitionCatalog.resolve(0xC777,0))
        assertEquals(8,OriginalTransitionCatalog.resolve(0xC77A,0))

        assertEquals(19,OriginalTransitionCatalog.resolve(0xC7A1,0))
        assertEquals(19,OriginalTransitionCatalog.resolve(0xC7A1,500))
        assertEquals(17,OriginalTransitionCatalog.resolve(0xC7A1,501))
        assertEquals(17,OriginalTransitionCatalog.resolve(0xC7A1,0xFFFF))

        assertEquals(17,OriginalTransitionCatalog.resolve(0xC7B6,300))
        assertEquals(24,OriginalTransitionCatalog.resolve(0xC7B6,301))
        assertEquals(24,OriginalTransitionCatalog.resolve(0xC7B6,4900))
        assertEquals(51,OriginalTransitionCatalog.resolve(0xC7B6,4901))

        assertEquals(31,OriginalTransitionCatalog.resolve(0xC7FE,100))
        assertEquals(27,OriginalTransitionCatalog.resolve(0xC7FE,101))
        assertEquals(27,OriginalTransitionCatalog.resolve(0xC7FE,1160))
        assertEquals(127,OriginalTransitionCatalog.resolve(0xC7FE,1161))

        assertEquals(34,OriginalTransitionCatalog.resolve(0xC84C,330))
        assertEquals(255,OriginalTransitionCatalog.resolve(0xC84C,331))
        assertEquals(255,OriginalTransitionCatalog.resolve(0xC84C,640))
        assertEquals(34,OriginalTransitionCatalog.resolve(0xC84C,641))

        assertTrue(OriginalTransitionCatalog.isNormalLocation(0))
        assertTrue(OriginalTransitionCatalog.isNormalLocation(49))
        assertFalse(OriginalTransitionCatalog.isNormalLocation(50))
        assertFalse(OriginalTransitionCatalog.isNormalLocation(255))

        val loc0=OriginalLocationCatalog[0]
        val loc15=OriginalLocationCatalog[15]
        val loc49=OriginalLocationCatalog[49]
        assertEquals(255,OriginalTransitionCatalog.resolve(loc0.leftTransitionCpu,0))
        assertEquals(122,OriginalTransitionCatalog.resolve(loc0.rightTransitionCpu,0))
        assertEquals(19,OriginalTransitionCatalog.resolve(loc15.rightTransitionCpu,400))
        assertEquals(17,OriginalTransitionCatalog.resolve(loc15.rightTransitionCpu,600))
        assertEquals(22,OriginalTransitionCatalog.resolve(loc49.leftTransitionCpu,0))
    }
}
