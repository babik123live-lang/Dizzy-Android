package com.dizzy.remake

import com.dizzy.remake.core.OriginalWorldXOriginCatalog as O
import org.junit.Assert.*
import org.junit.Test

class OriginalWorldXOriginCatalogTest {
    @Test fun originsPreserveGlobalXAcrossLocationChanges() {
        assertEquals(0,O.originX(0))
        assertEquals(0,O.originX(7))
        assertEquals(33664,O.originX(8))
        assertEquals(32768,O.originX(15))
        assertEquals(0,O.originX(16))
        assertEquals(35328,O.originX(17))
        assertEquals(36992,O.originX(18))
        assertEquals(31392,O.originX(19))
        assertEquals(28960,O.originX(29))
        assertEquals(43584,O.originX(30))
        assertEquals(32768,O.originX(31))
        assertEquals(34880,O.originX(45))
        assertEquals(38912,O.originX(49))

        assertEquals(160,O.globalX(0,160))
        assertEquals(33764,O.globalX(8,100))
        assertEquals(100,O.localXAfterLocationChange(8,100,8))
        assertEquals(228,O.localXAfterLocationChange(8,100,9))
        assertEquals(356,O.localXAfterLocationChange(8,100,10))
        assertEquals(1484,O.localXAfterLocationChange(18,100,17))
        assertEquals(1764,O.localXAfterLocationChange(18,100,19))

        val g=O.globalX(18,777)
        assertEquals(g,O.globalX(17,O.localXAfterLocationChange(18,777,17)))
        assertEquals(g,O.globalX(19,O.localXAfterLocationChange(18,777,19)))
        assertEquals(g,O.globalX(24,O.localXAfterLocationChange(18,777,24)))
    }
}
