package com.dizzy.remake.core

/**
 * 10-byte persistent-object record recovered from the European NES ROM.
 * Layout is based on the original loader used when an area is entered.
 */
data class OriginalObject(
    val areaId:Int,
    val x:Int,
    val subAreaId:Int,
    val y:Int,
    val spriteAddress:Int,
    val interactionId:Int,
    val descriptionAddress:Int,
    val interactionSubId:Int
) {
    companion object {
        fun decode(b:ByteArray, o:Int):OriginalObject {
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
