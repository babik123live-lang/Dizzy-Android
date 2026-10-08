package com.dizzy.remake

import com.dizzy.remake.core.OriginalStaticCollisionMap as C
import org.junit.Assert.*
import org.junit.Test

class OriginalStaticCollisionMapTest {
    @Test fun directFullMapLookupMatchesRomMapAndCollisionMasks() {
        val a=C.sample(0,0,0)
        assertEquals(95,a.mapCellId)
        assertEquals(81,a.tileId)
        assertFalse(a.blocked)

        val b=C.sample(0,100,180)
        assertEquals(45,b.mapCellId)
        assertEquals(93,b.tileId)
        assertTrue(b.blocked)

        val c=C.sample(0,160,160)
        assertEquals(21,c.mapCellId)
        assertEquals(69,c.tileId)
        assertFalse(c.blocked)

        val d=C.sample(16,0,0)
        assertEquals(0,d.mapCellId)
        assertEquals(0,d.tileId)
        assertFalse(d.blocked)

        val e=C.sample(16,64,64)
        assertEquals(18,e.mapCellId)
        assertEquals(1,e.tileId)
        assertTrue(e.blocked)

        val f=C.sample(18,1000,100)
        assertEquals(0,f.mapCellId)
        assertEquals(8,f.tileId)
        assertFalse(f.blocked)

        val g=C.sample(29,100,100)
        assertEquals(0,g.mapCellId)
        assertEquals(0,g.tileId)
        assertFalse(g.blocked)

        val h=C.sample(34,300,100)
        assertEquals(43,h.mapCellId)
        assertEquals(2,h.tileId)
        assertFalse(h.blocked)

        val i=C.sample(49,100,100)
        assertEquals(9,i.mapCellId)
        assertEquals(150,i.tileId)
        assertTrue(i.blocked)

        val j=C.sample(49,500,180)
        assertEquals(32,j.mapCellId)
        assertEquals(150,j.tileId)
        assertTrue(j.blocked)

        assertFalse(C.isBlocked(0,0,0))
        assertTrue(C.isBlocked(0,100,180))
        assertTrue(C.isBlocked(16,64,64))
        assertTrue(C.isBlocked(49,100,100))

        var bad=false
        try { C.sample(0,-1,0) } catch (_:IllegalArgumentException) { bad=true }
        assertTrue(bad)
        bad=false
        try { C.sample(0,480,0) } catch (_:IllegalArgumentException) { bad=true }
        assertTrue(bad)
        bad=false
        try { C.sample(0,0,192) } catch (_:IllegalArgumentException) { bad=true }
        assertTrue(bad)
    }
}
