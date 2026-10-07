package com.dizzy.remake.core

/**
 * Byte offsets of the 22-byte location descriptor consumed by original routine $C500.
 * Names are used only where the consumer has been traced. The two auxiliary
 * pointers are named only after tracing their original consumers.
 */
object OriginalLocationDescriptorLayout {
    const val BYTE_SIZE = 22
    const val WIDTH_COLUMNS32 = 0
    const val WORLD_ORIGIN_LO = 1
    const val WORLD_ORIGIN_HI = 2
    const val METATILE_BASE_LO = 3
    const val METATILE_BASE_HI = 4
    const val LEFT_TRANSITION_LO = 5
    const val LEFT_TRANSITION_HI = 6
    const val RIGHT_TRANSITION_LO = 7
    const val RIGHT_TRANSITION_HI = 8
    const val METATILE_BANK = 9
    const val COLLISION_MASK_LO = 10
    const val COLLISION_MASK_HI = 11
    const val PALETTE_DATA_LO = 12
    const val PALETTE_DATA_HI = 13
    const val CHR_UPLOAD_LIST_LO = 14
    const val CHR_UPLOAD_LIST_HI = 15
    const val TEXT_STREAM_LO = 16
    const val TEXT_STREAM_HI = 17
    const val ROUTINE_LIST_LO = 18
    const val ROUTINE_LIST_HI = 19
    const val MAP_STREAM_LO = 20
    const val MAP_STREAM_HI = 21

    fun word(source:ByteArray, offset:Int):Int {
        require(offset >= 0 && offset + 1 < source.size)
        return (source[offset].toInt() and 0xff) or ((source[offset+1].toInt() and 0xff) shl 8)
    }
}
