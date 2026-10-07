package com.dizzy.remake.core

/** Exact 10-byte record decoded from the ROM table; semantics intentionally unassigned. */
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
    companion object {
        const val BYTE_SIZE = 10
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
