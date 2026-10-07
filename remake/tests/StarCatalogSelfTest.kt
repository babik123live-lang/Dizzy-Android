package com.dizzy.remake.tests

import com.dizzy.remake.core.StarCatalog
import com.dizzy.remake.core.StarLayout
import com.dizzy.remake.core.StarSpriteVariant

fun main() {
    val all = StarCatalog.all
    check(StarCatalog.TOTAL == 250)
    check(StarCatalog.COUNTER_INITIAL == 250)
    check(StarCatalog.COUNTER_RAM == 0x0772)
    check(StarCatalog.PACKED_STATE_FIRST_RAM == 0x0751)
    check(StarCatalog.PACKED_STATE_LAST_RAM == 0x0770)
    check(all.size == 250)
    check(all.map { it.id } == (0 until 250).toList())
    check(all.map { it.id }.toSet().size == 250)

    val main = all.filter { it.layout == StarLayout.HORIZONTAL_GROUPED }
    check(main.size == 210)
    check(main.all { it.locationKeyRaw != null })
    check(main.mapNotNull { it.locationKeyRaw } == main.mapNotNull { it.locationKeyRaw }.sorted())
    check(main.mapNotNull { it.locationKeyRaw }.minOrNull() == 0)
    check(main.mapNotNull { it.locationKeyRaw }.maxOrNull() == 49)
    check(main.mapNotNull { it.locationKeyRaw }.toSet().size == 49)
    check(16 !in main.mapNotNull { it.locationKeyRaw }.toSet())
    check(main.minOf { it.worldAxis } == 38)
    check(main.maxOf { it.worldAxis } == 2828)
    check(main.minOf { it.fixedAxis } == 12)
    check(main.maxOf { it.fixedAxis } == 161)
    check(main.all { it.spriteVariant == StarSpriteVariant.MAIN_4F })

    val va = all.filter { it.layout == StarLayout.VERTICAL_A }
    val vb = all.filter { it.layout == StarLayout.VERTICAL_B }
    val vc = all.filter { it.layout == StarLayout.VERTICAL_C }
    check(va.size == 10)
    check(vb.size == 20)
    check(vc.size == 10)
    check(va.map { it.id } == (210 until 220).toList())
    check(vb.map { it.id } == (220 until 240).toList())
    check(vc.map { it.id } == (240 until 250).toList())
    check(va.all { it.locationKeyRaw == null && it.spriteVariant == StarSpriteVariant.MAIN_4F })
    check(vb.all { it.locationKeyRaw == null && it.spriteVariant == StarSpriteVariant.ALT_F0 })
    check(vc.all { it.locationKeyRaw == null && it.spriteVariant == StarSpriteVariant.MAIN_4F })
    check(StarCatalog.horizontalForLocationKey(0).size == 2)
    check(StarCatalog.horizontalForLocationKey(16).isEmpty())
    check(StarCatalog.horizontalForLocationKey(49).size == 3)
    check(all.first().worldAxis == 352 && all.first().fixedAxis == 81)
    check(all[209].locationKeyRaw == 49 && all[209].worldAxis == 459 && all[209].fixedAxis == 87)
    check(all[210].worldAxis == 448 && all[210].fixedAxis == 176)
    check(all.last().worldAxis == 4352 && all.last().fixedAxis == 128)
    println("star catalog self-test OK")
}
