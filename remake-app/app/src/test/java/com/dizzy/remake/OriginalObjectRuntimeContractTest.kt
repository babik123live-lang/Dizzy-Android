package com.dizzy.remake

import com.dizzy.remake.core.OriginalObjectRuntimeContract as R
import org.junit.Assert.*
import org.junit.Test

class OriginalObjectRuntimeContractTest {
    @Test fun loaderAndGraphicsSlotContractMatchesOriginal() {
        assertEquals(0xF550,R.SOURCE_CPU)
        assertEquals(64,R.FULL_RECORD_COUNT)
        assertEquals(10,R.SOURCE_RECORD_SIZE)
        assertEquals(0x05CF,R.CACHE_RAM)
        assertEquals(5,R.CACHE_RECORD_SIZE)
        assertEquals(65,R.CACHE_COPY_COUNT)
        assertEquals(0xF7D0,R.SENTINEL_CPU)
        assertEquals(0xFF,R.SENTINEL_FIRST_BYTE)
        assertEquals(0x059D,R.ACTIVE_RAM)
        assertEquals(5,R.ACTIVE_RECORD_SIZE)
        assertEquals(3,R.GRAPHICS_BANK)
        assertEquals(4,R.GRAPHICS_TILES_PER_OBJECT)
        assertEquals(0xD7,R.FIRST_GRAPHICS_TILE)
        assertEquals(0xFD,R.GRAPHICS_TILE_LIMIT)
        assertEquals(10,R.MAX_GRAPHICS_SLOTS)
        assertEquals(0xD7,R.graphicsTileForSlot(0))
        assertEquals(0xDB,R.graphicsTileForSlot(1))
        assertEquals(0xFB,R.graphicsTileForSlot(9))
        assertTrue(R.canAllocateGraphicsSlot(0))
        assertTrue(R.canAllocateGraphicsSlot(9))
        assertFalse(R.canAllocateGraphicsSlot(10))
    }
}
