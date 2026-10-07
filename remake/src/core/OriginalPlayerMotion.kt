package com.dizzy.remake.core

data class OriginalHorizontalStep(val fractional:Int,val coarsePixels:Int)

/**
 * Verified movement primitives from original bank-0 player code.
 *
 * Horizontal acceleration uses $CF (0..$30), table $870F and fractional
 * accumulator $D2. Vertical velocity is byte $AB; it is incremented once per
 * tick before being converted to a signed pixel delta by the $82F1-$831A path.
 */
object OriginalPlayerMotion {
    const val NORMAL_JUMP_VELOCITY_RAW = 0xF0 // -16
    const val SPECIAL_JUMP_VELOCITY_RAW = 0xF8 // -8
    const val HORIZONTAL_ACCEL_MAX = 0x30

    private val horizontalFractionTable=intArrayOf(
        0,0,0,5,10,15,20,25,30,35,40,45,50,55,60,65,70,75,80,85,90,95,100,105,
        110,115,120,125,130,135,140,145,150,155,160,165,170,175,180,185,190,195,
        200,205,210,215,220,225,0
    )

    fun horizontalStep(accelCounter:Int):OriginalHorizontalStep {
        require(accelCounter in 0..HORIZONTAL_ACCEL_MAX)
        val coarse=when(accelCounter) {
            0 -> 0
            HORIZONTAL_ACCEL_MAX -> 2
            else -> 1
        }
        return OriginalHorizontalStep(horizontalFractionTable[accelCounter],coarse)
    }

    fun nextAcceleration(current:Int,sameHeldDirection:Boolean):Int {
        require(current in 0..HORIZONTAL_ACCEL_MAX)
        return if(sameHeldDirection) {
            if(current==HORIZONTAL_ACCEL_MAX) current else current+1
        } else current ushr 1
    }

    fun signedVelocity(raw:Int):Int {
        require(raw in 0..255)
        return if(raw<0x80) raw else raw-0x100
    }

    /** Base vertical delta before the original optional extra half-speed shift. */
    fun verticalPixelDeltaBase(rawVelocity:Int):Int {
        val v=signedVelocity(rawVelocity)
        return if(v<0) Math.floorDiv(v,4) else v/4
    }

    fun nextVerticalVelocity(rawVelocity:Int):Int {
        require(rawVelocity in 0..255)
        return (rawVelocity+1) and 0xFF
    }
}
