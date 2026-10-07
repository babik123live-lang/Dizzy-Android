package com.dizzy.remake.core

data class OriginalTransitionStep(val thresholdX:Int,val destinationRaw:Int)

/**
 * Exact ROM transition tables used by $C748. The original chooses the first
 * entry whose thresholdX is >= the current 16-bit world X.
 */
object OriginalTransitionCatalog {
    private val raw = mapOf(
        0xC771 to intArrayOf(65535,255),
        0xC774 to intArrayOf(65535,122),
        0xC777 to intArrayOf(65535,9),
        0xC77A to intArrayOf(65535,8),
        0xC77D to intArrayOf(65535,10),
        0xC780 to intArrayOf(65535,9),
        0xC783 to intArrayOf(65535,11),
        0xC786 to intArrayOf(65535,10),
        0xC789 to intArrayOf(65535,12),
        0xC78C to intArrayOf(65535,11),
        0xC78F to intArrayOf(65535,13),
        0xC792 to intArrayOf(65535,12),
        0xC795 to intArrayOf(65535,14),
        0xC798 to intArrayOf(65535,13),
        0xC79B to intArrayOf(65535,15),
        0xC79E to intArrayOf(65535,14),
        0xC7A1 to intArrayOf(500,19,65535,17),
        0xC7AD to intArrayOf(65535,15),
        0xC7B0 to intArrayOf(1000,45,65535,18),
        0xC7B6 to intArrayOf(300,17,4900,24,65535,51),
        0xC7BF to intArrayOf(65535,21),
        0xC7C2 to intArrayOf(500,29,65535,15),
        0xC7C8 to intArrayOf(65535,126),
        0xC7CB to intArrayOf(65535,23),
        0xC7CE to intArrayOf(65535,255),
        0xC7D1 to intArrayOf(65535,18),
        0xC7D4 to intArrayOf(65535,22),
        0xC7D7 to intArrayOf(65535,21),
        0xC7DA to intArrayOf(65535,49),
        0xC7DD to intArrayOf(65535,21),
        0xC7E0 to intArrayOf(500,24,1300,20,65535,30),
        0xC7E9 to intArrayOf(1400,255,1500,33,65535,23),
        0xC7F2 to intArrayOf(65535,18),
        0xC7F5 to intArrayOf(65535,255),
        0xC7F8 to intArrayOf(65535,26),
        0xC7FB to intArrayOf(65535,25),
        0xC7FE to intArrayOf(100,31,1160,27,65535,127),
        0xC807 to intArrayOf(65535,26),
        0xC80A to intArrayOf(65535,28),
        0xC80D to intArrayOf(65535,27),
        0xC810 to intArrayOf(65535,29),
        0xC813 to intArrayOf(300,255,65535,124),
        0xC819 to intArrayOf(500,255,65535,19),
        0xC81F to intArrayOf(190,23,2500,255,65535,126),
        0xC828 to intArrayOf(65535,31),
        0xC82B to intArrayOf(2975,255,65535,26),
        0xC831 to intArrayOf(500,255,65535,30),
        0xC837 to intArrayOf(65535,255),
        0xC83A to intArrayOf(65535,33),
        0xC83D to intArrayOf(65535,32),
        0xC840 to intArrayOf(65535,50),
        0xC843 to intArrayOf(1000,255,65535,123),
        0xC849 to intArrayOf(65535,35),
        0xC84C to intArrayOf(330,34,640,255,65535,34),
        0xC855 to intArrayOf(65535,36),
        0xC858 to intArrayOf(65535,35),
        0xC85B to intArrayOf(65535,255),
        0xC85E to intArrayOf(65535,255),
        0xC861 to intArrayOf(65535,255),
        0xC864 to intArrayOf(65535,255),
        0xC867 to intArrayOf(65535,255),
        0xC86A to intArrayOf(65535,255),
        0xC86D to intArrayOf(65535,255),
        0xC870 to intArrayOf(65535,255),
        0xC873 to intArrayOf(65535,255),
        0xC876 to intArrayOf(65535,255),
        0xC879 to intArrayOf(65535,255),
        0xC87C to intArrayOf(65535,255),
        0xC87F to intArrayOf(65535,255),
        0xC882 to intArrayOf(65535,255),
        0xC885 to intArrayOf(65535,255),
        0xC888 to intArrayOf(65535,17),
        0xC88B to intArrayOf(65535,46),
        0xC88E to intArrayOf(200,125,65535,45),
        0xC894 to intArrayOf(65535,47),
        0xC897 to intArrayOf(65535,46),
        0xC89A to intArrayOf(65535,47),
        0xC89D to intArrayOf(65535,255),
        0xC8A0 to intArrayOf(65535,44),
        0xC8A3 to intArrayOf(65535,48),
        0xC8A6 to intArrayOf(65535,255),
        0xC8A9 to intArrayOf(65535,22),
        0xC8AC to intArrayOf(65535,255)
    )

    val tables:Map<Int,List<OriginalTransitionStep>> = raw.mapValues { (_,v) ->
        require(v.size % 2 == 0)
        (v.indices step 2).map { i -> OriginalTransitionStep(v[i],v[i+1]) }
    }

    init {
        require(tables.size == 83)
        require(tables.values.sumOf { it.size } == 104)
        require(tables.values.all { it.isNotEmpty() && it.last().thresholdX == 0xFFFF })
        require(tables.values.all { t -> t.zipWithNext().all { (a,b) -> a.thresholdX <= b.thresholdX } })
    }

    fun table(cpuAddress:Int):List<OriginalTransitionStep> =
        tables[cpuAddress] ?: error("Unknown original transition table " + cpuAddress.toString(16))

    fun resolve(cpuAddress:Int, worldX:Int):Int {
        require(worldX in 0..0xFFFF)
        return table(cpuAddress).first { it.thresholdX >= worldX }.destinationRaw
    }

    fun isNormalLocation(destinationRaw:Int):Boolean = destinationRaw in 0 until OriginalLocationCatalog.COUNT
}
