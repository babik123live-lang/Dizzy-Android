package com.dizzy.remake.core

/**
 * Verified rendering contract for one streamed 32-pixel world column.
 *
 * The original builds a PPU update list in page $0100. For each of six
 * metatile rows it writes one attribute byte and four tile IDs. The 24 tile
 * IDs are sent vertically, followed by tile $DF, while the six attribute bytes
 * go to the matching attribute-table column.
 */
object OriginalMapRenderContract {
    const val MAP_ROWS = 6
    const val TILES_PER_METATILE_COLUMN = 4
    const val STREAMED_TILE_COUNT = MAP_ROWS * TILES_PER_METATILE_COLUMN
    const val BOTTOM_TILE = 0xDF
    const val NAMETABLE0_HIGH = 0x20
    const val NAMETABLE1_HIGH = 0x24
    const val ATTRIBUTE0_HIGH = 0x23
    const val ATTRIBUTE1_HIGH = 0x27
    const val ATTRIBUTE_BASE_LOW = 0xC0

    fun nametableHigh(worldTileColumn:Int):Int =
        if ((worldTileColumn and 0x20) == 0) NAMETABLE0_HIGH else NAMETABLE1_HIGH

    fun nametableColumn(worldTileColumn:Int):Int = worldTileColumn and 0x1F

    fun attributeHigh(worldTileColumn:Int):Int =
        if ((worldTileColumn and 0x20) == 0) ATTRIBUTE0_HIGH else ATTRIBUTE1_HIGH

    fun attributeColumnLow(worldTileColumn:Int):Int =
        ATTRIBUTE_BASE_LOW + ((worldTileColumn and 0x1F) ushr 2)

    fun metatileColumn(worldTileColumn:Int):Int = worldTileColumn and 0x03
}
