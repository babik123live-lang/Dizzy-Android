package com.dizzy.remake.core

enum class StarLayout { HORIZONTAL_GROUPED, VERTICAL_A, VERTICAL_B, VERTICAL_C }
enum class StarSpriteVariant { MAIN_4F, ALT_F0 }

data class StarPlacement(
    val id:Int,
    val layout:StarLayout,
    val locationKeyRaw:Int?,
    val worldAxis:Int,
    val fixedAxis:Int,
    val spriteVariant:StarSpriteVariant
)

/**
 * ROM-derived collectible-star data. locationKeyRaw is the runtime location
 * selector compared with RAM $93; it is deliberately not a RoomCatalog id.
 */
object StarCatalog {
    const val TOTAL = 250
    const val MAIN_HORIZONTAL_COUNT = 210
    const val COUNTER_INITIAL = 250
    const val COUNTER_RAM = 0x0772
    const val PACKED_STATE_FIRST_RAM = 0x0751
    const val PACKED_STATE_LAST_RAM = 0x0770

    private val mainRaw = intArrayOf(
        0, 352, 81, 0, 44, 109, 1, 60, 128, 1, 304, 141, 2, 60, 128, 2, 277, 109,
        3, 164, 136, 3, 429, 141, 4, 348, 128, 4, 198, 141, 5, 648, 72, 5, 43, 141,
        6, 480, 32, 6, 105, 141, 7, 392, 72, 7, 141, 45, 8, 328, 128, 8, 824, 112,
        9, 448, 144, 9, 1000, 144, 9, 838, 77, 10, 1256, 16, 10, 264, 144, 10, 783, 53,
        10, 963, 149, 10, 1113, 85, 11, 644, 112, 11, 1232, 144, 11, 180, 21, 11, 539, 21,
        11, 774, 85, 11, 1448, 101, 12, 560, 112, 12, 900, 144, 12, 1059, 37, 12, 1498, 77,
        12, 1796, 133, 13, 616, 16, 13, 400, 112, 13, 1708, 112, 13, 964, 117, 13, 1461, 93,
        14, 626, 144, 14, 1088, 32, 14, 1680, 16, 14, 307, 117, 14, 916, 125, 14, 2073, 93,
        15, 728, 136, 15, 192, 136, 15, 2376, 136, 15, 1932, 136, 15, 553, 125, 15, 1141, 21,
        15, 1439, 101, 15, 1941, 53, 15, 2088, 141, 17, 120, 136, 17, 1200, 136, 17, 1622, 136,
        17, 804, 136, 17, 398, 109, 17, 1380, 93, 18, 1996, 132, 18, 1236, 146, 18, 2828, 28,
        18, 108, 37, 18, 606, 93, 18, 878, 93, 18, 1456, 61, 18, 2275, 143, 18, 2560, 93,
        19, 220, 56, 19, 600, 104, 19, 1084, 104, 19, 1384, 45, 19, 834, 109, 20, 52, 96,
        20, 544, 120, 20, 292, 61, 21, 224, 136, 21, 496, 136, 21, 1772, 24, 21, 832, 109,
        21, 985, 66, 21, 1300, 30, 22, 508, 112, 22, 880, 24, 22, 1576, 16, 22, 1968, 96,
        22, 1336, 24, 22, 832, 109, 22, 1024, 77, 22, 293, 102, 22, 1551, 158, 23, 1832, 88,
        23, 584, 40, 23, 128, 136, 23, 996, 120, 23, 492, 125, 23, 748, 125, 23, 1311, 61,
        23, 1680, 29, 24, 1668, 152, 24, 1328, 12, 24, 344, 136, 24, 159, 141, 24, 789, 53,
        24, 946, 53, 24, 1800, 93, 25, 660, 72, 25, 356, 104, 25, 977, 77, 26, 180, 152,
        26, 720, 40, 26, 124, 117, 26, 424, 161, 26, 394, 45, 26, 1020, 109, 26, 558, 113,
        26, 704, 113, 27, 1128, 24, 27, 576, 136, 27, 188, 72, 28, 576, 40, 28, 1026, 49,
        29, 332, 104, 29, 1676, 104, 29, 2404, 120, 29, 1915, 109, 29, 1158, 109, 30, 212, 16,
        30, 1312, 120, 30, 1852, 88, 30, 2216, 86, 30, 1496, 125, 30, 2508, 77, 30, 899, 141,
        30, 147, 125, 31, 356, 104, 31, 1776, 136, 31, 2304, 104, 31, 1910, 125, 31, 957, 125,
        31, 2776, 61, 31, 2576, 77, 32, 876, 48, 32, 108, 112, 32, 526, 45, 33, 120, 48,
        33, 776, 104, 33, 370, 84, 33, 861, 14, 34, 328, 72, 34, 719, 77, 34, 830, 41,
        34, 110, 161, 35, 616, 12, 35, 1036, 40, 35, 1201, 109, 35, 427, 109, 35, 983, 141,
        35, 40, 77, 35, 751, 77, 35, 945, 33, 36, 640, 32, 36, 1188, 120, 36, 38, 125,
        36, 374, 125, 36, 1008, 125, 37, 252, 88, 38, 124, 88, 39, 124, 88, 40, 252, 88,
        41, 124, 88, 42, 252, 88, 43, 124, 88, 44, 964, 120, 44, 666, 125, 44, 143, 125,
        44, 162, 13, 44, 444, 13, 45, 1112, 24, 45, 784, 120, 45, 1852, 32, 45, 1500, 136,
        45, 176, 49, 45, 264, 121, 46, 416, 24, 46, 784, 120, 46, 552, 29, 46, 1532, 29,
        46, 1170, 129, 46, 1560, 141, 46, 1785, 133, 47, 1552, 128, 47, 240, 61, 47, 823, 77,
        47, 1282, 149, 47, 1635, 45, 47, 432, 77, 47, 1856, 62, 48, 716, 88, 48, 252, 104,
        48, 444, 96, 48, 53, 77, 48, 932, 141, 49, 220, 136, 49, 71, 60, 49, 459, 87,
    )
    private val verticalARaw = intArrayOf(
        448, 176, 448, 80, 520, 42, 768, 176, 896, 100, 1072, 80, 1216, 160, 1344, 74, 1544, 160,
        1648, 74,
    )
    private val verticalBRaw = intArrayOf(
        8056, 186, 7712, 170, 7392, 170, 6992, 138, 6592, 170, 6320, 164, 5840, 234, 5552, 202, 4656, 106,
        4096, 202, 3776, 138, 3328, 138, 3072, 202, 2560, 170, 2048, 202, 1792, 202, 1536, 138, 1024, 186,
        768, 42, 416, 106,
    )
    private val verticalCRaw = intArrayOf(
        480, 96, 768, 176, 1072, 80, 1616, 106, 1984, 80, 2232, 42, 2608, 106, 3200, 120, 3704, 128,
        4352, 128,
    )

    private fun horizontal(): List<StarPlacement> =
        (0 until MAIN_HORIZONTAL_COUNT).map { i ->
            val o=i*3
            StarPlacement(i,StarLayout.HORIZONTAL_GROUPED,mainRaw[o],mainRaw[o+1],mainRaw[o+2],StarSpriteVariant.MAIN_4F)
        }

    private fun vertical(start:Int, layout:StarLayout, raw:IntArray, variant:StarSpriteVariant): List<StarPlacement> =
        (0 until raw.size/2).map { i ->
            val o=i*2
            StarPlacement(start+i,layout,null,raw[o],raw[o+1],variant)
        }

    val all: List<StarPlacement> =
        horizontal() +
        vertical(210,StarLayout.VERTICAL_A,verticalARaw,StarSpriteVariant.MAIN_4F) +
        vertical(220,StarLayout.VERTICAL_B,verticalBRaw,StarSpriteVariant.ALT_F0) +
        vertical(240,StarLayout.VERTICAL_C,verticalCRaw,StarSpriteVariant.MAIN_4F)

    init {
        require(mainRaw.size == MAIN_HORIZONTAL_COUNT*3)
        require(verticalARaw.size == 20 && verticalBRaw.size == 40 && verticalCRaw.size == 20)
        require(all.size == TOTAL)
        require(all.map { it.id } == (0 until TOTAL).toList())
    }

    fun horizontalForLocationKey(value:Int): List<StarPlacement> =
        all.filter { it.layout == StarLayout.HORIZONTAL_GROUPED && it.locationKeyRaw == value }
}
