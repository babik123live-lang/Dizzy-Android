package com.dizzy.remake

import com.dizzy.remake.core.OriginalMetatileCatalog as M
import org.junit.Assert.*
import org.junit.Test

class OriginalMetatileCatalogTest {
    @Test fun embeddedDefinitionsMatchVerifiedRomRecords() {
        assertEquals(13,M.setCount())

        val a=M.definition(0,95)
        assertEquals(listOf(81,81,201,121,81,81,78,78,81,81,78,78,81,81,201,146),a.tiles)
        assertEquals(136,a.attributeByte)

        val b=M.definition(0,27)
        assertEquals(listOf(121,122,122,122,78,123,123,123,78,123,123,123,124,125,125,125),b.tiles)
        assertEquals(170,b.attributeByte)

        val c=M.definition(16,0)
        assertEquals(listOf(0,0,0,0,0,0,0,0,0,63,64,65,0,66,1,1),c.tiles)
        assertEquals(0,c.attributeByte)

        val d=M.definition(16,47)
        assertEquals(listOf(1,1,98,0,79,109,84,0,1,1,83,0,92,93,106,0),d.tiles)
        assertEquals(0,d.attributeByte)

        val e=M.definition(18,76)
        assertEquals(listOf(8,8,8,8,8,8,8,8,136,137,122,125,133,144,128,129),e.tiles)
        assertEquals(64,e.attributeByte)

        val f=M.definition(29,9)
        assertEquals(listOf(0,0,0,9,0,9,1,3,10,5,4,4,6,15,15,16),f.tiles)
        assertEquals(170,f.attributeByte)

        val g=M.definition(34,81)
        assertEquals(List(16){59},g.tiles)
        assertEquals(170,g.attributeByte)

        val h=M.definition(49,1)
        assertEquals(listOf(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,10),h.tiles)
        assertEquals(170,h.attributeByte)

        assertEquals(a,M.at(0,0,0))
        assertEquals(c,M.at(16,0,0))
        assertEquals(h,M.at(49,0,0))

        var bad=false
        try { M.definition(16,48) } catch (_:IllegalArgumentException) { bad=true }
        assertTrue(bad)
    }
}
