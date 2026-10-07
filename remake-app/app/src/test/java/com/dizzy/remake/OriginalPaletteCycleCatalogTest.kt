package com.dizzy.remake

import com.dizzy.remake.core.OriginalPaletteCycleCatalog as P
import org.junit.Assert.*
import org.junit.Test

class OriginalPaletteCycleCatalogTest {
    @Test fun paletteCyclesMatchOriginalBaa0Layout() {
        assertEquals(0xB64A,P.paletteCpuForLocation(0))
        assertEquals(0xB68E,P.paletteCpuForLocation(8))
        assertEquals(0xB6B8,P.paletteCpuForLocation(15))
        assertEquals(0xBA08,P.paletteCpuForLocation(16))
        assertEquals(0xB76A,P.paletteCpuForLocation(18))
        assertEquals(0xB7F2,P.paletteCpuForLocation(29))
        assertEquals(0xB92C,P.paletteCpuForLocation(34))
        assertEquals(0xB7AE,P.paletteCpuForLocation(49))

        val p0=P.forLocation(0)
        assertEquals(listOf(16,60,32),p0.spriteTail)
        assertEquals(3,p0.backgroundFrames13.size)
        assertTrue(p0.backgroundFrames13.all{it.size==13})
        assertEquals(
            listOf(14,44,22,6,14,32,41,6,14,6,22,38,14,6,29,16),
            p0.backgroundPalette16(0))
        assertEquals(
            listOf(14,1,22,6,14,32,41,6,14,6,22,38,14,6,29,16),
            p0.backgroundPalette16(1))
        assertEquals(
            listOf(14,14,22,6,14,32,41,6,14,6,22,38,14,6,29,16),
            p0.backgroundPalette16(2))
        assertEquals(0x0E,p0.backgroundPalette16(0)[4])
        assertEquals(0x0E,p0.backgroundPalette16(0)[8])
        assertEquals(0x0E,p0.backgroundPalette16(0)[12])

        val p16=P.forLocation(16)
        assertEquals(listOf(18,5,39),p16.spriteTail)
        assertEquals(p16.backgroundPalette16(0),p16.backgroundPalette16(1))
        assertEquals(p16.backgroundPalette16(1),p16.backgroundPalette16(2))

        val p18=P.forLocation(18)
        assertEquals(listOf(18,5,39),p18.spriteTail)
        assertNotEquals(p18.backgroundPalette16(0),p18.backgroundPalette16(1))
        assertNotEquals(p18.backgroundPalette16(1),p18.backgroundPalette16(2))

        val p49=P.forLocation(49)
        assertEquals(listOf(18,5,39),p49.spriteTail)
        assertEquals(p49.backgroundPalette16(1),p49.backgroundPalette16(2))
        assertTrue((0 until 50).all{key->
            P.forLocation(key).spriteTail.all{it in 0..0x3F} &&
            (0..2).all{phase->P.forLocation(key).backgroundPalette16(phase).all{it in 0..0x3F}}
        })

        var bad=false
        try { p0.backgroundPalette16(3) } catch (_:IllegalArgumentException) { bad=true }
        assertTrue(bad)
    }
}
