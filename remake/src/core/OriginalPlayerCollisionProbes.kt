package com.dizzy.remake.core

data class OriginalCollisionProbe(val dx:Int,val dy:Int,val sourceCpu:Int)

/**
 * Point offsets around player reference RAM $A2/$A3 used by bank-0 movement.
 * Names intentionally describe code paths, not inferred body-part semantics.
 */
object OriginalPlayerCollisionProbes {
    val verticalSnap = listOf(
        OriginalCollisionProbe(-4,-1,0x834B),
        OriginalCollisionProbe(4,-1,0x8359)
    )
    val sideMotion = listOf(
        OriginalCollisionProbe(-8,-12,0x8543),
        OriginalCollisionProbe(8,-12,0x8543)
    )
    val upperMotion = listOf(
        OriginalCollisionProbe(-4,-26,0x857D),
        OriginalCollisionProbe(4,-26,0x8591)
    )
    val lowerMotion = listOf(
        OriginalCollisionProbe(-4,0,0x85C0),
        OriginalCollisionProbe(4,0,0x85CD)
    )

    val all = verticalSnap + sideMotion + upperMotion + lowerMotion

    init {
        require(all.size==8)
        require(all.map{it.dx}.minOrNull()==-8)
        require(all.map{it.dx}.maxOrNull()==8)
        require(all.map{it.dy}.minOrNull()==-26)
        require(all.map{it.dy}.maxOrNull()==0)
    }
}
