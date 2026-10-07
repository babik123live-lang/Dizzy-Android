package com.dizzy.remake

import com.dizzy.remake.core.OriginalPlayerMotion as M
import org.junit.Assert.*
import org.junit.Test

class OriginalPlayerMotionTest {
    @Test fun movementPrimitivesMatchOriginalBankZeroMath() {
        assertEquals(0xF0,M.NORMAL_JUMP_VELOCITY_RAW)
        assertEquals(0xF8,M.SPECIAL_JUMP_VELOCITY_RAW)
        assertEquals(0x30,M.HORIZONTAL_ACCEL_MAX)

        assertEquals(0,M.horizontalStep(0).fractional)
        assertEquals(0,M.horizontalStep(0).coarsePixels)
        assertEquals(0,M.horizontalStep(1).fractional)
        assertEquals(1,M.horizontalStep(1).coarsePixels)
        assertEquals(5,M.horizontalStep(3).fractional)
        assertEquals(1,M.horizontalStep(3).coarsePixels)
        assertEquals(225,M.horizontalStep(47).fractional)
        assertEquals(1,M.horizontalStep(47).coarsePixels)
        assertEquals(0,M.horizontalStep(48).fractional)
        assertEquals(2,M.horizontalStep(48).coarsePixels)

        assertEquals(1,M.nextAcceleration(0,true))
        assertEquals(48,M.nextAcceleration(47,true))
        assertEquals(48,M.nextAcceleration(48,true))
        assertEquals(24,M.nextAcceleration(48,false))
        assertEquals(7,M.nextAcceleration(15,false))
        assertEquals(0,M.nextAcceleration(1,false))

        assertEquals(-16,M.signedVelocity(0xF0))
        assertEquals(-8,M.signedVelocity(0xF8))
        assertEquals(-1,M.signedVelocity(0xFF))
        assertEquals(0,M.signedVelocity(0))
        assertEquals(127,M.signedVelocity(0x7F))

        assertEquals(-4,M.verticalPixelDeltaBase(0xF0))
        assertEquals(-4,M.verticalPixelDeltaBase(0xF1))
        assertEquals(-3,M.verticalPixelDeltaBase(0xF4))
        assertEquals(-2,M.verticalPixelDeltaBase(0xF8))
        assertEquals(-1,M.verticalPixelDeltaBase(0xFC))
        assertEquals(-1,M.verticalPixelDeltaBase(0xFF))
        assertEquals(0,M.verticalPixelDeltaBase(0x00))
        assertEquals(0,M.verticalPixelDeltaBase(0x03))
        assertEquals(1,M.verticalPixelDeltaBase(0x04))
        assertEquals(4,M.verticalPixelDeltaBase(0x10))

        assertEquals(0xF1,M.nextVerticalVelocity(0xF0))
        assertEquals(0xF9,M.nextVerticalVelocity(0xF8))
        assertEquals(0x00,M.nextVerticalVelocity(0xFF))

        var v=M.NORMAL_JUMP_VELOCITY_RAW
        var y=0
        repeat(16) {
            v=M.nextVerticalVelocity(v)
            y+=M.verticalPixelDeltaBase(v)
        }
        assertEquals(0,v)
        assertEquals(-36,y)
    }
}
