package com.dizzy.remake.core

/**
 * Descriptor bytes 1/2 are the per-location world-X origin loaded into
 * $91/$92 by $C500. On ordinary location changes the original preserves
 * global X by computing:
 * newLocalX = oldLocalX + oldOriginX - newOriginX.
 */
object OriginalWorldXOriginCatalog {
    private val origins=intArrayOf(
        0,0,0,0,0,0,0,0,
        33664,33536,33408,33280,33152,33024,32896,32768,
        0,35328,36992,31392,42528,37568,37568,41792,39904,
        35712,35712,35712,35712,28960,43584,32768,40704,40704,
        32768,32768,32768,32768,32768,32768,32768,32768,32768,
        32768,32768,34880,34880,34880,32768,38912
    )

    init {
        require(origins.size==OriginalLocationCatalog.COUNT)
    }

    fun originX(locationKey:Int):Int {
        require(locationKey in 0 until OriginalLocationCatalog.COUNT)
        return origins[locationKey]
    }

    fun globalX(locationKey:Int, localX:Int):Int =
        originX(locationKey)+localX

    fun localXAfterLocationChange(oldKey:Int,oldLocalX:Int,newKey:Int):Int =
        globalX(oldKey,oldLocalX)-originX(newKey)
}
