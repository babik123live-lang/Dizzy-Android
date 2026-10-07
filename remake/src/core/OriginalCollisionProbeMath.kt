package com.dizzy.remake.core

/**
 * Coordinate decomposition used by original point-collision routines
 * $DF0D/$DFA5/$DFBF. The active map contains six 32px rows.
 */
object OriginalCollisionProbeMath {
    const val ACTIVE_HEIGHT_PX = 192
    const val METATILE_SIZE_PX = 32
    const val TILE_SIZE_PX = 8

    fun metatileRow(y:Int):Int {
        require(y in 0 until ACTIVE_HEIGHT_PX)
        return y ushr 5
    }

    fun metatileColumn(combinedX:Int):Int {
        require(combinedX >= 0)
        return combinedX ushr 5
    }

    fun tileRowWithinMetatile(y:Int):Int {
        require(y in 0 until ACTIVE_HEIGHT_PX)
        return (y and 0x18) ushr 3
    }

    fun tileColumnWithinMetatile(combinedX:Int):Int {
        require(combinedX >= 0)
        return (combinedX and 0x1F) ushr 3
    }

    fun tileIndexWithinMetatile(combinedX:Int,y:Int):Int =
        tileRowWithinMetatile(y)*4 + tileColumnWithinMetatile(combinedX)
}
