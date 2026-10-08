package com.dizzy.remake.core

/**
 * Verified contracts of the original persistent-object loader.
 *
 * $8834 copies bytes 0..4 of 64 complete records plus the first five bytes
 * beginning at $F7D0 into 5-byte cache records at $05CF. The 65th cache entry
 * starts with $FF and is a sentinel; it is not a 65th 10-byte object.
 *
 * $88DE scans only the 64 complete records. Matching byte 0 against RAM $93
 * activates a record. $8927 assigns a four-tile CHR slot and compacts bytes
 * 1..3 plus runtime slot/source-index metadata into the active-object area.
 */
object OriginalObjectRuntimeContract {
    const val SOURCE_CPU = 0xF550
    const val FULL_RECORD_COUNT = 64
    const val SOURCE_RECORD_SIZE = 10
    const val CACHE_RAM = 0x05CF
    const val CACHE_RECORD_SIZE = 5
    const val CACHE_COPY_COUNT = 65
    const val SENTINEL_CPU = 0xF7D0
    const val SENTINEL_FIRST_BYTE = 0xFF

    const val ACTIVE_RAM = 0x059D
    const val ACTIVE_RECORD_SIZE = 5
    const val GRAPHICS_BANK = 3
    const val GRAPHICS_TILES_PER_OBJECT = 4
    const val FIRST_GRAPHICS_TILE = 0xD7
    const val GRAPHICS_TILE_LIMIT = 0xFD
    const val MAX_GRAPHICS_SLOTS = 10

    /**
     * Active record layout produced by $895A:
     * 0/1 = original world-X low/high; 2 = original Y;
     * 3 = runtime graphics-slot index; 4 = original source-record index.
     */
    const val ACTIVE_WORLD_X_LO = 0
    const val ACTIVE_WORLD_X_HI = 1
    const val ACTIVE_Y = 2
    const val ACTIVE_GRAPHICS_SLOT = 3
    const val ACTIVE_SOURCE_INDEX = 4

    fun graphicsTileForSlot(slot:Int):Int {
        require(slot >= 0)
        return FIRST_GRAPHICS_TILE + slot * GRAPHICS_TILES_PER_OBJECT
    }

    fun canAllocateGraphicsSlot(slot:Int):Boolean =
        slot in 0 until MAX_GRAPHICS_SLOTS && graphicsTileForSlot(slot) < GRAPHICS_TILE_LIMIT
}
