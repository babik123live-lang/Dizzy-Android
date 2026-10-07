package com.dizzy.remake

import com.dizzy.remake.core.OriginalMapStripComposer
import com.dizzy.remake.core.OriginalMetatile
import org.junit.Assert.*
import org.junit.Test

class OriginalMapStripComposerTest {
    @Test fun composesTheSame24TileVerticalStripAsOriginalRenderer() {
        val rows=(0 until 6).map { r ->
            OriginalMetatile((0 until 16).map { r*32+it },0x11*r)
        }
        val c0=OriginalMapStripComposer.compose(rows,0)
        assertEquals(24,c0.tileIds.size)
        assertEquals(6,c0.attributeBytes.size)
        assertEquals(listOf(0,4,8,12),c0.tileIds.subList(0,4))
        assertEquals(listOf(32,36,40,44),c0.tileIds.subList(4,8))
        assertEquals(listOf(64,68,72,76),c0.tileIds.subList(8,12))
        assertEquals(listOf(96,100,104,108),c0.tileIds.subList(12,16))
        assertEquals(listOf(128,132,136,140),c0.tileIds.subList(16,20))
        assertEquals(listOf(160,164,168,172),c0.tileIds.subList(20,24))
        assertEquals(listOf(0x00,0x11,0x22,0x33,0x44,0x55),c0.attributeBytes)

        val c1=OriginalMapStripComposer.compose(rows,1)
        assertEquals(listOf(1,5,9,13),c1.tileIds.subList(0,4))
        assertEquals(listOf(33,37,41,45),c1.tileIds.subList(4,8))
        val c2=OriginalMapStripComposer.compose(rows,2)
        assertEquals(listOf(2,6,10,14),c2.tileIds.subList(0,4))
        val c3=OriginalMapStripComposer.compose(rows,3)
        assertEquals(listOf(3,7,11,15),c3.tileIds.subList(0,4))
        assertEquals(listOf(163,167,171,175),c3.tileIds.subList(20,24))

        var bad=false
        try { OriginalMapStripComposer.compose(rows.take(5),0) } catch (_:IllegalArgumentException) { bad=true }
        assertTrue(bad)
        bad=false
        try { OriginalMapStripComposer.compose(rows,4) } catch (_:IllegalArgumentException) { bad=true }
        assertTrue(bad)
    }
}
