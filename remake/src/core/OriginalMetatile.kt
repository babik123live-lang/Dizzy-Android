package com.dizzy.remake.core

/**
 * One ROM-derived 32x32 map-cell definition.
 * Bytes 0..15 are four rows of four 8x8 tile IDs. Byte 16 is kept raw until
 * its exact original runtime meaning is proven.
 */
data class OriginalMetatile(
    val tiles: List<Int>,
    val raw17: Int
) {
    init {
        require(tiles.size == 16)
        require(tiles.all { it in 0..255 })
        require(raw17 in 0..255)
    }

    fun tile(row:Int, column:Int):Int {
        require(row in 0..3 && column in 0..3)
        return tiles[row * 4 + column]
    }

    fun column(column:Int):List<Int> {
        require(column in 0..3)
        return (0..3).map { row -> tile(row, column) }
    }

    companion object {
        const val BYTE_SIZE = 17
        fun decode(source:ByteArray, offset:Int=0):OriginalMetatile {
            require(offset >= 0 && offset + BYTE_SIZE <= source.size)
            val tiles=(0 until 16).map { source[offset+it].toInt() and 0xff }
            return OriginalMetatile(tiles, source[offset+16].toInt() and 0xff)
        }
    }
}
