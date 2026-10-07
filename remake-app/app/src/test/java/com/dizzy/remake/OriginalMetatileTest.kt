package com.dizzy.remake

import com.dizzy.remake.core.OriginalMetatile
import org.junit.Assert.*
import org.junit.Test

class OriginalMetatileTest {
    @Test fun decodesExactRomCellLayout() {
        val sample=byteArrayOf(
            0x01,0x02,0x02,0x02,
            0x03,0x04,0x04,0x04,
            0x03,0x51,0x06,0x07,
            0x03,0x08,0x09,0x0A,
            0x6A
        )
        val m=OriginalMetatile.decode(sample)
        assertEquals(17,OriginalMetatile.BYTE_SIZE)
        assertEquals(16,m.tiles.size)
        assertEquals(0x6A,m.raw17)
        assertEquals(listOf(1,2,2,2),m.tiles.subList(0,4))
        assertEquals(listOf(3,4,4,4),m.tiles.subList(4,8))
        assertEquals(listOf(3,0x51,6,7),m.tiles.subList(8,12))
        assertEquals(listOf(3,8,9,10),m.tiles.subList(12,16))
        assertEquals(listOf(1,3,3,3),m.column(0))
        assertEquals(listOf(2,4,0x51,8),m.column(1))
        assertEquals(listOf(2,4,6,9),m.column(2))
        assertEquals(listOf(2,4,7,10),m.column(3))
        assertEquals(1,m.tile(0,0))
        assertEquals(10,m.tile(3,3))

        var bad=false
        try { OriginalMetatile.decode(byteArrayOf(1,2,3)) } catch (_:IllegalArgumentException) { bad=true }
        assertTrue(bad)

        bad=false
        try { m.tile(4,0) } catch (_:IllegalArgumentException) { bad=true }
        assertTrue(bad)
    }
}
