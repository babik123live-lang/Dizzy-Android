package com.dizzy.remake

import com.dizzy.remake.core.OriginalScriptSpriteCatalog
import org.junit.Assert.*
import org.junit.Test

class OriginalScriptSpriteCatalogTest {
    @Test fun placementsMatchExtractedF498Records() {
        val all=OriginalScriptSpriteCatalog.all
        assertEquals(100,all.size)
        assertEquals(24,all.minOf{it.worldX})
        assertEquals(2504,all.maxOf{it.worldX})
        assertEquals(20,all.minOf{it.y})
        assertEquals(152,all.maxOf{it.y})
        assertEquals(2,OriginalScriptSpriteCatalog.forLocation(0).size)
        assertEquals(3,OriginalScriptSpriteCatalog.forLocation(1).size)
        assertEquals(6,OriginalScriptSpriteCatalog.forLocation(3).size)
        assertEquals(0,OriginalScriptSpriteCatalog.forLocation(16).size)
        assertEquals(4,OriginalScriptSpriteCatalog.forLocation(29).size)
        assertEquals(6,OriginalScriptSpriteCatalog.forLocation(31).size)
        assertEquals(8,OriginalScriptSpriteCatalog.forLocation(45).size)
        assertEquals(11,OriginalScriptSpriteCatalog.forLocation(46).size)
        assertEquals(4,OriginalScriptSpriteCatalog.forLocation(48).size)
        assertEquals(408,all[0].worldX)
        assertEquals(88,all[0].y)
        assertEquals(440,all[1].worldX)
        assertEquals(88,all[1].y)
        assertTrue(all.all{it.locationKey in 0 until 50})
        assertTrue(all.all{it.worldX>=0})
        assertTrue(all.all{it.y in 0..255})
    }
}
