package com.dizzy.remake

import com.dizzy.remake.core.OriginalCollisionProbeMath as M
import org.junit.Assert.*
import org.junit.Test

class OriginalCollisionProbeMathTest {
    @Test fun decomposesPointExactlyLikeOriginalCollisionLookup() {
        assertEquals(192,M.ACTIVE_HEIGHT_PX)
        assertEquals(32,M.METATILE_SIZE_PX)
        assertEquals(8,M.TILE_SIZE_PX)

        assertEquals(0,M.metatileRow(0))
        assertEquals(0,M.metatileRow(31))
        assertEquals(1,M.metatileRow(32))
        assertEquals(5,M.metatileRow(191))

        assertEquals(0,M.metatileColumn(0))
        assertEquals(0,M.metatileColumn(31))
        assertEquals(1,M.metatileColumn(32))
        assertEquals(7,M.metatileColumn(255))
        assertEquals(8,M.metatileColumn(256))

        assertEquals(0,M.tileRowWithinMetatile(0))
        assertEquals(1,M.tileRowWithinMetatile(8))
        assertEquals(2,M.tileRowWithinMetatile(16))
        assertEquals(3,M.tileRowWithinMetatile(24))
        assertEquals(3,M.tileRowWithinMetatile(31))
        assertEquals(0,M.tileRowWithinMetatile(32))

        assertEquals(0,M.tileColumnWithinMetatile(0))
        assertEquals(1,M.tileColumnWithinMetatile(8))
        assertEquals(2,M.tileColumnWithinMetatile(16))
        assertEquals(3,M.tileColumnWithinMetatile(24))
        assertEquals(3,M.tileColumnWithinMetatile(31))
        assertEquals(0,M.tileColumnWithinMetatile(32))

        assertEquals(0,M.tileIndexWithinMetatile(0,0))
        assertEquals(3,M.tileIndexWithinMetatile(24,0))
        assertEquals(4,M.tileIndexWithinMetatile(0,8))
        assertEquals(10,M.tileIndexWithinMetatile(16,16))
        assertEquals(15,M.tileIndexWithinMetatile(31,31))
        assertEquals(0,M.tileIndexWithinMetatile(32,32))

        var bad=false
        try { M.metatileRow(192) } catch (_:IllegalArgumentException) { bad=true }
        assertTrue(bad)
    }
}
