package com.dizzy.remake

import com.dizzy.remake.core.OriginalLocationDescriptorLayout as L
import org.junit.Assert.*
import org.junit.Test

class OriginalLocationDescriptorLayoutTest {
    @Test fun offsetsMatchOriginalLoaderContract() {
        assertEquals(22,L.BYTE_SIZE)
        assertEquals(0,L.WIDTH_COLUMNS32)
        assertEquals(1,L.WORLD_ORIGIN_LO)
        assertEquals(3,L.METATILE_BASE_LO)
        assertEquals(5,L.LEFT_TRANSITION_LO)
        assertEquals(7,L.RIGHT_TRANSITION_LO)
        assertEquals(9,L.METATILE_BANK)
        assertEquals(10,L.COLLISION_MASK_LO)
        assertEquals(12,L.PALETTE_DATA_LO)
        assertEquals(14,L.CHR_UPLOAD_LIST_LO)
        assertEquals(16,L.TEXT_STREAM_LO)
        assertEquals(18,L.ROUTINE_LIST_LO)
        assertEquals(20,L.MAP_STREAM_LO)
        val room0=byteArrayOf(
            0x0f,0x00,0x00,0x01,0x80.toByte(),0x71,0xc7.toByte(),0x74,0xc7.toByte(),0x05,
            0x74,0xb6.toByte(),0x4a,0xb6.toByte(),0xaa.toByte(),0xa8.toByte(),
            0x38,0x97.toByte(),0x98.toByte(),0xa8.toByte(),0x01,0x80.toByte())
        assertEquals(0x8001,L.word(room0,L.METATILE_BASE_LO))
        assertEquals(0xC771,L.word(room0,L.LEFT_TRANSITION_LO))
        assertEquals(0xC774,L.word(room0,L.RIGHT_TRANSITION_LO))
        assertEquals(0xB674,L.word(room0,L.AUX10_LO))
        assertEquals(0xB64A,L.word(room0,L.AUX12_LO))
        assertEquals(0xA8AA,L.word(room0,L.CHR_UPLOAD_LIST_LO))
        assertEquals(0x9738,L.word(room0,L.TEXT_STREAM_LO))
        assertEquals(0xA898,L.word(room0,L.ROUTINE_LIST_LO))
        assertEquals(0x8001,L.word(room0,L.MAP_STREAM_LO))
    }
}
