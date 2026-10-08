package com.dizzy.remake.core

/**
 * Exact 10-byte persistent-object record decoded from the ROM.
 *
 * The original bank-0 loader proves bytes 0..5 have these roles:
 * raw0 = runtime location key, raw1/raw2 = little-endian world X,
 * raw3 = sprite Y, raw45le = source pointer for a four-tile CHR upload.
 * raw6 selects one of 32 behavior handlers. raw78le points to the bank-9
 * inventory/display record used by the original item screen. raw9 is a
 * behavior-specific parameter; handlers 1 and 3 use it as a variant index.
 */
data class OriginalObject(
    val raw0:Int,
    val raw1:Int,
    val raw2:Int,
    val raw3:Int,
    val raw45le:Int,
    val raw6:Int,
    val raw78le:Int,
    val raw9:Int
) {
    val locationKey:Int get() = raw0
    val worldX:Int get() = raw1 or (raw2 shl 8)
    val spriteY:Int get() = raw3
    val graphicsSourceCpu:Int get() = raw45le
    val behaviorIndex:Int get() = raw6
    val inventoryRecordCpu:Int get() = raw78le
    val behaviorParameter:Int get() = raw9

    companion object {
        const val BYTE_SIZE = 10
        fun decode(b:ByteArray, o:Int):OriginalObject {
            require(o >= 0 && o + BYTE_SIZE <= b.size) { "10-byte ROM record exceeds source bounds" }
            fun u(i:Int)=b[o+i].toInt() and 0xff
            return OriginalObject(
                u(0), u(1), u(2), u(3),
                u(4) or (u(5) shl 8),
                u(6),
                u(7) or (u(8) shl 8),
                u(9)
            )
        }
    }
}
