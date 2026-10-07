package com.dizzy.remake.core

data class OriginalScriptRangeGate(
    val locationKey:Int,
    val scriptOpIndex:Int,
    val worldXStart:Int,
    val worldXEndExclusive:Int,
    val rawParameter:Int
)

/**
 * The four five-byte records consumed by original handler $F4C1/$BB38.
 * $BB38 compares player world X against [start,end) before applying rawParameter.
 */
object OriginalScriptRangeGateCatalog {
    val all=listOf(
        OriginalScriptRangeGate(19,1,100,500,176),
        OriginalScriptRangeGate(24,1,804,928,0),
        OriginalScriptRangeGate(29,7,0,200,144),
        OriginalScriptRangeGate(31,7,0,184,176)
    )

    init {
        require(all.size==4)
        require(all.all{it.locationKey in 0 until OriginalLocationCatalog.COUNT})
        require(all.all{it.worldXStart < it.worldXEndExclusive})
        require(all.all{it.rawParameter in 0..255})
    }

    fun forLocation(key:Int)=all.filter{it.locationKey==key}
}
