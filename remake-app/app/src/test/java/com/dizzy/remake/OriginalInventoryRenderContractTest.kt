package com.dizzy.remake

import com.dizzy.remake.core.OriginalInventoryRenderContract as R
import com.dizzy.remake.core.OriginalObjectIdentityCatalog
import org.junit.Assert.*
import org.junit.Test

class OriginalInventoryRenderContractTest {
    @Test fun inventoryUploadMatchesOriginalF0e0Path() {
        assertEquals(3,R.SOURCE_BANK)
        assertEquals(3,R.HEADER_BYTES)
        assertEquals(16,R.TILE_COUNT)
        assertEquals(1,R.PATTERN_TABLE)
        assertEquals(4,R.SLOT_COUNT)
        assertEquals(0x70,R.destinationTile(0))
        assertEquals(0x80,R.destinationTile(1))
        assertEquals(0x90,R.destinationTile(2))
        assertEquals(0xA0,R.destinationTile(3))
        assertEquals(0x864C,R.payloadCpu(OriginalObjectIdentityCatalog[9]))
        assertEquals(0x8586,R.payloadCpu(OriginalObjectIdentityCatalog[10]))
        assertEquals(0x8004,R.payloadCpu(OriginalObjectIdentityCatalog[0]))
        var bad=false
        try { R.destinationTile(4) } catch (_:IllegalArgumentException) { bad=true }
        assertTrue(bad)
    }
}
