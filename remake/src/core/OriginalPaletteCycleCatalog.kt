package com.dizzy.remake.core

data class OriginalPaletteCycle(
    val spriteTail: List<Int>,
    val backgroundFrames13: List<List<Int>>
) {
    init {
        require(spriteTail.size==3)
        require(backgroundFrames13.size==3)
        require(backgroundFrames13.all{it.size==13})
        require((spriteTail+backgroundFrames13.flatten()).all{it in 0..0x3F})
    }

    /**
     * Original BAA0 expands 13 bytes to a full 16-byte background palette by
     * inserting $0E at palette slots 4, 8 and 12.
     */
    fun backgroundPalette16(phase:Int):List<Int> {
        require(phase in 0..2)
        val f=backgroundFrames13[phase]
        return listOf(f[0],f[1],f[2],f[3],0x0E,
            f[4],f[5],f[6],0x0E,
            f[7],f[8],f[9],0x0E,
            f[10],f[11],f[12])
    }
}

/**
 * ROM-derived per-location palette data consumed by bank-1 BAA0.
 * spriteTail is written to PPU $3F1D-$3F1F. The selected background frame is
 * expanded to 16 colors and written to $3F00-$3F0F.
 */
object OriginalPaletteCycleCatalog {
    private val paletteCpuByLocation=intArrayOf(
        46666,46666,46666,46666,46666,46666,46666,46666,
        46734,46734,46734,46734,46734,46734,46734,46776,
        47624,46844,46954,46844,47514,47022,47022,47200,
        46886,47268,47268,47268,47268,47090,47090,47132,
        47336,47336,47404,47404,47404,47666,47666,47666,
        47666,47666,47666,47708,47446,47514,47514,47556,
        47446,47022
    )

    private val raw=mapOf(
        0xB64A to intArrayOf(16,60,32,14,44,22,6,32,41,6,6,22,38,6,29,16,14,1,22,6,32,41,6,6,22,38,6,29,16,14,14,22,6,32,41,6,6,22,38,6,29,16),
        0xB68E to intArrayOf(14,16,35,44,25,41,14,39,38,14,22,38,14,25,22,14,1,9,25,14,38,22,14,6,22,14,9,6,14,14,9,25,14,38,22,14,6,22,14,9,6,14),
        0xB6B8 to intArrayOf(14,38,32,44,25,41,14,39,21,14,22,38,14,25,22,14,1,9,25,14,38,5,14,6,22,14,9,6,14,14,9,25,14,22,5,14,6,22,14,9,6,14),
        0xB6FC to intArrayOf(14,41,55,14,25,41,44,25,41,28,32,28,44,22,38,44,14,9,25,1,9,25,12,16,12,1,6,22,1,14,9,25,14,9,25,12,0,12,14,6,22,14),
        0xB726 to intArrayOf(14,41,55,14,25,41,44,25,41,20,32,20,44,22,38,44,14,9,25,1,9,25,4,16,4,1,6,22,1,14,9,25,14,9,25,4,0,4,14,6,22,14),
        0xB76A to intArrayOf(18,5,39,14,38,39,44,41,25,44,38,0,16,32,28,44,14,22,38,1,25,9,1,22,45,0,16,12,1,14,22,38,14,25,9,14,22,45,0,0,1,14),
        0xB7AE to intArrayOf(18,5,39,14,0,16,28,41,25,28,0,16,39,39,38,28,14,45,0,12,25,9,12,45,0,38,38,22,12,14,45,0,12,25,9,12,45,0,38,38,22,12),
        0xB7F2 to intArrayOf(14,38,32,44,16,0,14,25,41,14,32,28,14,54,38,6,1,0,45,14,9,25,14,16,12,14,38,22,14,14,45,29,14,9,25,14,0,12,14,38,22,14),
        0xB81C to intArrayOf(14,17,38,44,16,0,14,25,41,14,32,28,14,54,38,6,1,0,45,14,9,25,14,16,12,14,38,22,14,14,45,29,14,9,25,14,0,12,14,38,22,14),
        0xB860 to intArrayOf(18,5,39,1,9,25,14,16,0,14,22,38,14,9,22,14,1,9,25,14,16,0,14,22,38,14,9,22,14,14,7,23,14,0,45,14,6,22,14,7,6,14),
        0xB8A4 to intArrayOf(14,17,38,14,6,22,44,28,32,44,6,22,38,20,40,41,14,14,6,1,12,16,1,14,6,22,4,24,25,14,14,6,14,12,0,14,14,6,22,4,24,25),
        0xB8E8 to intArrayOf(18,5,39,44,22,38,14,25,41,14,32,16,14,32,22,14,1,6,22,14,9,25,14,16,0,14,16,6,14,14,6,22,14,9,25,14,0,45,14,0,6,14),
        0xB92C to intArrayOf(18,5,39,14,52,36,20,38,22,14,44,36,20,22,36,20,14,36,20,4,22,6,14,1,20,4,6,20,4,14,36,20,4,22,6,14,14,20,4,6,20,4),
        0xB956 to intArrayOf(18,5,39,14,32,16,0,38,22,6,44,16,0,22,16,0,14,32,16,0,38,22,6,1,16,0,22,16,0,14,32,16,0,38,22,6,14,16,0,22,16,0),
        0xB99A to intArrayOf(9,25,41,14,9,25,40,25,41,28,22,38,28,22,38,14,14,9,25,40,25,41,12,22,38,12,22,38,14,14,9,25,40,25,41,12,22,38,12,22,38,14),
        0xB9C4 to intArrayOf(18,5,39,14,9,25,40,25,41,21,22,38,21,22,38,14,14,9,25,40,25,41,21,22,38,21,22,38,14,14,9,25,40,25,41,21,22,38,21,22,38,14),
        0xBA08 to intArrayOf(18,5,39,14,6,22,38,38,22,6,14,14,14,14,14,14,14,6,22,38,38,22,6,14,14,14,14,14,14,14,6,22,38,38,22,6,14,14,14,14,14,14),
        0xBA32 to intArrayOf(18,5,39,14,32,16,0,38,22,6,22,16,6,32,22,6,14,32,16,0,38,22,6,22,16,6,32,22,6,14,32,16,0,38,22,6,22,16,6,32,22,6),
        0xBA5C to intArrayOf(14,16,25,14,32,16,0,38,22,6,22,16,6,32,22,6,14,32,16,0,38,22,6,22,16,6,32,22,6,14,32,16,0,38,22,6,22,16,6,32,22,6)
    )

    private val cycles:Map<Int,OriginalPaletteCycle> = raw.mapValues { (_,v) ->
        require(v.size==42)
        OriginalPaletteCycle(
            v.slice(0..2),
            listOf(v.slice(3..15),v.slice(16..28),v.slice(29..41))
        )
    }

    init {
        require(paletteCpuByLocation.size==OriginalLocationCatalog.COUNT)
        require(cycles.size==19)
        require(paletteCpuByLocation.all{it in cycles})
    }

    fun paletteCpuForLocation(locationKey:Int):Int {
        require(locationKey in 0 until OriginalLocationCatalog.COUNT)
        return paletteCpuByLocation[locationKey]
    }

    fun forLocation(locationKey:Int):OriginalPaletteCycle =
        cycles[paletteCpuForLocation(locationKey)] ?: error("Unknown palette cycle")
}
