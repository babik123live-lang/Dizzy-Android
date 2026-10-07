package com.dizzy.remake

import com.dizzy.remake.core.OriginalScriptRangeGateCatalog
import org.junit.Assert.*
import org.junit.Test

class OriginalScriptRangeGateCatalogTest {
    @Test fun gatesMatchTheFourExtractedF4C1Records() {
        val all=OriginalScriptRangeGateCatalog.all
        assertEquals(4,all.size)
        assertEquals(listOf(19,24,29,31),all.map{it.locationKey})
        assertEquals(listOf(100,804,0,0),all.map{it.worldXStart})
        assertEquals(listOf(500,928,200,184),all.map{it.worldXEndExclusive})
        assertEquals(listOf(176,0,144,176),all.map{it.rawParameter})
        assertEquals(1,OriginalScriptRangeGateCatalog.forLocation(19).size)
        assertEquals(1,OriginalScriptRangeGateCatalog.forLocation(24).size)
        assertEquals(1,OriginalScriptRangeGateCatalog.forLocation(29).size)
        assertEquals(1,OriginalScriptRangeGateCatalog.forLocation(31).size)
        assertTrue(OriginalScriptRangeGateCatalog.forLocation(0).isEmpty())
        assertTrue(all.all{it.worldXStart < it.worldXEndExclusive})
        assertTrue(all.all{it.rawParameter in 0..255})
    }
}
