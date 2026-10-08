package com.dizzy.remake.core

/**
 * Verified inventory-item graphics path used after bank-9 routine $AC6A.
 * The inventory record's first word points into bank 3. The first three bytes
 * are copied to the display command buffer; the source is then advanced by
 * three and sixteen CHR tiles are decoded into pattern table 1.
 */
object OriginalInventoryRenderContract {
    const val SOURCE_BANK = 3
    const val HEADER_BYTES = 3
    const val TILE_COUNT = 16
    const val PATTERN_TABLE = 1
    const val SLOT_COUNT = 4
    const val FIRST_DESTINATION_TILE = 0x70
    const val DESTINATION_TILE_STRIDE = 0x10

    fun destinationTile(slot:Int):Int {
        require(slot in 0 until SLOT_COUNT)
        return FIRST_DESTINATION_TILE + slot * DESTINATION_TILE_STRIDE
    }

    fun payloadCpu(identity:OriginalObjectIdentity):Int =
        identity.inventoryGraphicsCpu + HEADER_BYTES
}
