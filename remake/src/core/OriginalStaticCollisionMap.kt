package com.dizzy.remake.core

data class OriginalCollisionSample(
    val locationKey:Int,
    val localX:Int,
    val y:Int,
    val mapCellId:Int,
    val tileId:Int,
    val blocked:Boolean
)

/**
 * Native full-map equivalent of the original tile collision lookup.
 *
 * The NES code reaches the same tile through its scrolling cache. Here the
 * location-local coordinate addresses the complete ROM-derived row-major map
 * directly, then applies the exact per-location collision bit mask.
 */
object OriginalStaticCollisionMap {
    fun sample(locationKey:Int,localX:Int,y:Int):OriginalCollisionSample {
        require(locationKey in 0 until OriginalLocationCatalog.COUNT)
        val widthPx=OriginalLocationCatalog[locationKey].widthPx
        require(localX in 0 until widthPx)
        require(y in 0 until OriginalCollisionProbeMath.ACTIVE_HEIGHT_PX)

        val cellColumn=localX ushr 5
        val cellRow=y ushr 5
        val cellId=OriginalMapCellCatalog.cellId(locationKey,cellRow,cellColumn)
        val metatile=OriginalMetatileCatalog.definition(locationKey,cellId)
        val tileColumn=(localX and 0x1f) ushr 3
        val tileRow=(y and 0x1f) ushr 3
        val tileId=metatile.tile(tileRow,tileColumn)
        val blocked=OriginalCollisionMaskCatalog.isBlockedTile(locationKey,tileId)
        return OriginalCollisionSample(locationKey,localX,y,cellId,tileId,blocked)
    }

    fun isBlocked(locationKey:Int,localX:Int,y:Int):Boolean =
        sample(locationKey,localX,y).blocked
}
