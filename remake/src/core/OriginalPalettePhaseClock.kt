package com.dizzy.remake.core

/**
 * Palette-phase selection performed at original $D4E4-$D50A.
 * $03D4 increments only when byte counter $48 wraps; it selects phases
 * 0 for 00..9F, 1 for A0..C7, and 2 for C8..FF.
 */
object OriginalPalettePhaseClock {
    const val FAST_COUNTER_RAM = 0x0048
    const val SLOW_COUNTER_RAM = 0x03D4
    const val PHASE_RAM = 0x03CD

    fun phase(slowCounter:Int):Int {
        require(slowCounter in 0..255)
        return when {
            slowCounter < 0xA0 -> 0
            slowCounter < 0xC8 -> 1
            else -> 2
        }
    }
}
