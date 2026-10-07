package com.dizzy.remake

import com.dizzy.remake.core.OriginalScriptSpriteRenderContract as R
import org.junit.Assert.*
import org.junit.Test

class OriginalScriptSpriteRenderContractTest {
    @Test fun animationMathMatchesOriginal87EB() {
        assertEquals(0xF498,R.HANDLER_CPU)
        assertEquals(0x87EB,R.RENDER_CPU)
        assertEquals(-8,R.TOP_Y_OFFSET)
        assertEquals(0,R.BOTTOM_Y_OFFSET)

        val f0=R.frame(0)
        assertEquals(0x84,f0.topTile)
        assertEquals(0x85,f0.bottomTile)
        assertEquals(0x01,f0.attributes)

        val f8=R.frame(8)
        assertEquals(0x86,f8.topTile)
        assertEquals(0x87,f8.bottomTile)
        assertEquals(0x01,f8.attributes)

        val f16=R.frame(16)
        assertEquals(0x84,f16.topTile)
        assertEquals(0x85,f16.bottomTile)
        assertEquals(0x41,f16.attributes)

        val f20=R.frame(20)
        assertEquals(0x84,f20.topTile)
        assertEquals(0x85,f20.bottomTile)
        assertEquals(0x41,f20.attributes)

        var bad=false
        try { R.frame(-1) } catch (_:IllegalArgumentException) { bad=true }
        assertTrue(bad)
        bad=false
        try { R.frame(256) } catch (_:IllegalArgumentException) { bad=true }
        assertTrue(bad)
    }
}
