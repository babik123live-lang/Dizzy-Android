package com.dizzy.remake.core

/**
 * ROM-derived per-location collision membership tables.
 *
 * The original collision path indexes tileId/8, then tests
 * 0x80 shr (tileId and 7). All map tile IDs are <=207, so each table is
 * exactly 26 bytes (208 bits).
 */
object OriginalCollisionMaskCatalog {
    const val TILE_DOMAIN_SIZE = 208
    const val BYTES_PER_MASK = 26

    private val collisionMaskCpuByLocation = intArrayOf(
        46708,46708,46708,46708,46708,46708,46708,46708,
        46818,46818,46818,46818,46818,46818,46818,46818,
        47598,46928,46996,46928,47598,47064,47064,47242,
        46928,47310,47310,47310,47310,47174,47174,47174,
        47378,47378,47488,47488,47488,47750,47750,47750,
        47750,47750,47750,47750,47488,47598,47598,47598,
        47488,47064
    )

    private val masks = mapOf(
        0xB674 to intArrayOf(96,16,0,0,0,0,0,224,3,66,24,134,0,0,0,12,0,0,32,0,0,0,0,0,1,64),
        0xB6E2 to intArrayOf(65,224,61,0,0,0,0,0,0,0,0,80,0,0,12,0,0,0,4,4,0,0,0,0,0,0),
        0xB750 to intArrayOf(0,0,0,0,127,255,255,0,0,0,0,3,193,3,64,30,193,112,0,16,2,0,112,0,14,7),
        0xB794 to intArrayOf(0,0,63,255,255,255,255,255,255,255,255,255,255,251,79,192,29,129,128,0,0,4,0,0,62,105),
        0xB7D8 to intArrayOf(127,255,127,255,255,255,127,253,253,132,57,255,255,255,254,205,149,76,246,0,0,128,0,0,4,0),
        0xB846 to intArrayOf(0,0,63,240,63,255,252,0,0,0,0,0,0,0,0,0,0,0,30,64,6,0,0,0,0,0),
        0xB88A to intArrayOf(64,96,0,0,0,0,0,12,0,0,0,0,0,0,2,0,0,0,53,16,0,0,4,0,1,85),
        0xB8CE to intArrayOf(29,240,51,184,129,94,255,249,192,0,0,0,59,225,241,93,208,8,3,0,0,227,0,0,0,2),
        0xB912 to intArrayOf(3,3,192,0,0,0,0,0,0,0,0,240,0,3,3,0,0,0,0,0,0,0,0,0,0,0),
        0xB980 to intArrayOf(0,0,0,0,0,2,0,13,251,238,0,0,0,0,0,0,0,6,0,0,0,20,128,1,0,0),
        0xB9EE to intArrayOf(127,128,0,126,127,255,223,203,243,208,0,0,0,0,0,0,0,0,0,16,2,2,64,5,7,128),
        0xBA86 to intArrayOf(32,64,9,48,74,64,0,22,8,8,9,2,200,0,56,0,0,64,0,0,0,0,0,0,0,0)
    )

    init {
        require(collisionMaskCpuByLocation.size == OriginalLocationCatalog.COUNT)
        require(masks.size == 12)
        require(masks.values.all { it.size == BYTES_PER_MASK })
        require(collisionMaskCpuByLocation.all { it in masks })
    }

    fun maskCpuForLocation(locationKey:Int):Int {
        require(locationKey in 0 until OriginalLocationCatalog.COUNT)
        return collisionMaskCpuByLocation[locationKey]
    }

    fun isBlockedTile(locationKey:Int, tileId:Int):Boolean =
        isBlockedTileByMask(maskCpuForLocation(locationKey), tileId)

    fun isBlockedTileByMask(maskCpu:Int, tileId:Int):Boolean {
        require(tileId in 0 until TILE_DOMAIN_SIZE)
        val data=masks[maskCpu] ?: error("Unknown collision mask")
        return (data[tileId ushr 3] and (0x80 ushr (tileId and 7))) != 0
    }

    fun blockedTilesForLocation(locationKey:Int):List<Int> =
        (0 until TILE_DOMAIN_SIZE).filter { isBlockedTile(locationKey,it) }
}
