package com.dizzy.remake

import com.dizzy.remake.core.OriginalMapRenderContract as R
import org.junit.Assert.*
import org.junit.Test

class OriginalMapRenderContractTest {
    @Test fun streamedColumnsMatchOriginalPpuAddressing() {
        assertEquals(6,R.MAP_ROWS)
        assertEquals(4,R.TILES_PER_METATILE_COLUMN)
        assertEquals(24,R.STREAMED_TILE_COUNT)
        assertEquals(0xDF,R.BOTTOM_TILE)
        assertEquals(0x20,R.NAMETABLE0_HIGH)
        assertEquals(0x24,R.NAMETABLE1_HIGH)
        assertEquals(0x23,R.ATTRIBUTE0_HIGH)
        assertEquals(0x27,R.ATTRIBUTE1_HIGH)
        assertEquals(0xC0,R.ATTRIBUTE_BASE_LOW)

        assertEquals(0x20,R.nametableHigh(0))
        assertEquals(0x20,R.nametableHigh(31))
        assertEquals(0x24,R.nametableHigh(32))
        assertEquals(0x24,R.nametableHigh(63))
        assertEquals(0x20,R.nametableHigh(64))

        assertEquals(0,R.nametableColumn(0))
        assertEquals(31,R.nametableColumn(31))
        assertEquals(0,R.nametableColumn(32))
        assertEquals(31,R.nametableColumn(63))

        assertEquals(0x23,R.attributeHigh(0))
        assertEquals(0x23,R.attributeHigh(31))
        assertEquals(0x27,R.attributeHigh(32))
        assertEquals(0x27,R.attributeHigh(63))

        assertEquals(0xC0,R.attributeColumnLow(0))
        assertEquals(0xC0,R.attributeColumnLow(3))
        assertEquals(0xC1,R.attributeColumnLow(4))
        assertEquals(0xC7,R.attributeColumnLow(31))
        assertEquals(0xC0,R.attributeColumnLow(32))

        assertEquals(0,R.metatileColumn(0))
        assertEquals(1,R.metatileColumn(1))
        assertEquals(2,R.metatileColumn(2))
        assertEquals(3,R.metatileColumn(3))
        assertEquals(0,R.metatileColumn(4))
        assertEquals(3,R.metatileColumn(31))
    }
}
