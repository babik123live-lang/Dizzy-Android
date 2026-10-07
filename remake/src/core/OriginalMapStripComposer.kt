package com.dizzy.remake.core

data class OriginalMapStrip(
    val tileIds: List<Int>,
    val attributeBytes: List<Int>
)

/**
 * Pure reconstruction of the six-row column assembly performed by $C903/$C96C.
 */
object OriginalMapStripComposer {
    fun compose(rows:List<OriginalMetatile>, fineColumn:Int):OriginalMapStrip {
        require(rows.size == OriginalMapRenderContract.MAP_ROWS)
        require(fineColumn in 0..3)
        val tiles=rows.flatMap { it.column(fineColumn) }
        val attributes=rows.map { it.attributeByte }
        require(tiles.size == OriginalMapRenderContract.STREAMED_TILE_COUNT)
        require(attributes.size == OriginalMapRenderContract.MAP_ROWS)
        return OriginalMapStrip(tiles,attributes)
    }
}
