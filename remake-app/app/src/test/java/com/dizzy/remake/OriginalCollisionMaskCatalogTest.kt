package com.dizzy.remake

import com.dizzy.remake.core.OriginalCollisionMaskCatalog as C
import org.junit.Assert.*
import org.junit.Test

class OriginalCollisionMaskCatalogTest {
    @Test fun masksMatchOriginalBitOrderAndLocationSelection() {
        assertEquals(208,C.TILE_DOMAIN_SIZE)
        assertEquals(26,C.BYTES_PER_MASK)
        assertEquals(0xB674,C.maskCpuForLocation(0))
        assertEquals(0xB6E2,C.maskCpuForLocation(8))
        assertEquals(0xB9EE,C.maskCpuForLocation(16))
        assertEquals(0xB794,C.maskCpuForLocation(18))
        assertEquals(0xB88A,C.maskCpuForLocation(23))
        assertEquals(0xB846,C.maskCpuForLocation(29))
        assertEquals(0xB980,C.maskCpuForLocation(34))
        assertEquals(0xBA86,C.maskCpuForLocation(37))
        assertEquals(0xB7D8,C.maskCpuForLocation(49))

        assertTrue(C.isBlockedTile(0,1))
        assertTrue(C.isBlockedTile(0,2))
        assertTrue(C.isBlockedTile(0,11))
        assertFalse(C.isBlockedTile(0,0))
        assertFalse(C.isBlockedTile(0,3))
        assertTrue(C.isBlockedTile(8,89))
        assertFalse(C.isBlockedTile(8,29))
        assertTrue(C.isBlockedTile(16,63))
        assertFalse(C.isBlockedTile(16,68))
        assertTrue(C.isBlockedTile(18,18))
        assertFalse(C.isBlockedTile(18,17))
        assertTrue(C.isBlockedTile(23,199))
        assertFalse(C.isBlockedTile(23,198))
        assertTrue(C.isBlockedTile(29,34))
        assertFalse(C.isBlockedTile(29,33))
        assertTrue(C.isBlockedTile(34,46))
        assertFalse(C.isBlockedTile(34,45))
        assertTrue(C.isBlockedTile(37,2))
        assertFalse(C.isBlockedTile(37,1))
        assertTrue(C.isBlockedTile(49,1))
        assertFalse(C.isBlockedTile(49,48))

        assertEquals(20,C.blockedTilesForLocation(0).size)
        assertEquals(16,C.blockedTilesForLocation(8).size)
        assertEquals(60,C.blockedTilesForLocation(16).size)
        assertEquals(117,C.blockedTilesForLocation(18).size)
        assertEquals(124,C.blockedTilesForLocation(49).size)

        var bad=false
        try { C.isBlockedTile(0,208) } catch (_:IllegalArgumentException) { bad=true }
        assertTrue(bad)
    }
}
