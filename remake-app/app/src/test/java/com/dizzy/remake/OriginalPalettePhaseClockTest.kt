package com.dizzy.remake

import com.dizzy.remake.core.OriginalPalettePhaseClock as C
import org.junit.Assert.assertEquals
import org.junit.Test

class OriginalPalettePhaseClockTest {
    @Test fun thresholdsMatchOriginalD4e4Code() {
        assertEquals(0x0048,C.FAST_COUNTER_RAM)
        assertEquals(0x03D4,C.SLOW_COUNTER_RAM)
        assertEquals(0x03CD,C.PHASE_RAM)
        assertEquals(0,C.phase(0x00))
        assertEquals(0,C.phase(0x9F))
        assertEquals(1,C.phase(0xA0))
        assertEquals(1,C.phase(0xC7))
        assertEquals(2,C.phase(0xC8))
        assertEquals(2,C.phase(0xE0))
        assertEquals(2,C.phase(0xE1))
        assertEquals(2,C.phase(0xFF))
    }
}
