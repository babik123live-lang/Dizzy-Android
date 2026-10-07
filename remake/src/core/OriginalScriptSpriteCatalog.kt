package com.dizzy.remake.core

data class OriginalScriptSpritePlacement(
    val locationKey:Int,
    val scriptOpIndex:Int,
    val worldX:Int,
    val y:Int
)

/**
 * The 100 three-byte placements consumed by original handler $F498.
 * $F498 loads worldX low/high and Y, then $87EB converts worldX to screen X
 * and emits two adjacent animation tiles through $D87F.
 */
object OriginalScriptSpriteCatalog {
    private val raw=intArrayOf(
        0,1,408,88, 0,2,440,88, 1,0,312,88, 1,1,344,88, 1,2,40,72, 2,0,216,88, 2,1,248,88, 2,2,168,72,
        3,0,40,40, 3,1,104,40, 3,2,264,40, 3,3,360,40, 3,4,440,88, 3,5,472,88, 4,0,216,88, 4,1,248,88,
        5,0,216,88, 5,1,248,88, 6,0,56,88, 6,1,88,88, 6,2,392,72, 7,0,392,40, 8,0,454,120, 9,0,230,120,
        9,1,466,120, 9,2,850,24, 10,0,818,24, 11,0,658,88, 11,1,946,120, 11,2,1106,120, 11,3,1298,24, 11,4,1510,56,
        12,0,178,120, 12,1,614,24, 12,2,914,120, 12,3,1190,56, 12,4,1426,56, 13,0,306,88, 13,1,914,88, 13,2,1606,88,
        13,3,1766,88, 14,0,486,120, 14,1,946,88, 14,2,1222,24, 14,3,2022,56, 15,2,998,24, 15,3,1074,88, 15,4,1926,24,
        29,0,2216,52, 29,1,1992,52, 29,2,1160,52, 29,3,136,20, 30,0,776,84, 30,1,1096,84, 30,2,1448,52, 30,3,1544,52,
        31,0,2504,20, 31,1,2056,52, 31,2,1672,84, 31,3,1608,84, 31,4,648,84, 31,5,328,52, 34,2,408,68, 35,3,216,32,
        35,4,696,64, 36,3,24,100, 36,4,120,100, 36,5,472,36, 36,6,656,92, 44,2,80,24, 44,3,376,100, 44,4,472,100,
        44,5,568,100, 45,3,1004,71, 45,4,780,71, 45,5,716,55, 45,6,652,39, 45,7,588,23, 45,8,364,23, 45,9,236,23,
        45,10,332,119, 46,3,1388,23, 46,4,1132,23, 46,5,812,23, 46,6,620,23, 46,7,364,23, 46,8,204,119, 46,9,332,119,
        46,10,524,119, 46,11,716,119, 46,12,972,119, 46,13,1228,119, 47,3,332,38, 47,4,492,38, 47,5,716,38, 47,6,908,38,
        48,2,48,24, 48,3,176,24, 48,4,624,152, 48,5,880,88
    )

    val all:List<OriginalScriptSpritePlacement>=(0 until raw.size/4).map { i ->
        val o=i*4
        OriginalScriptSpritePlacement(raw[o],raw[o+1],raw[o+2],raw[o+3])
    }

    init {
        require(raw.size == 400)
        require(all.size == 100)
        require(all.all { it.locationKey in 0 until OriginalLocationCatalog.COUNT })
        require(all.minOf { it.worldX } == 24)
        require(all.maxOf { it.worldX } == 2504)
        require(all.minOf { it.y } == 20)
        require(all.maxOf { it.y } == 152)
    }

    fun forLocation(key:Int):List<OriginalScriptSpritePlacement> =
        all.filter { it.locationKey == key }
}
