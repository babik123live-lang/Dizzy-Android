package com.dizzy.remake.tests

import com.dizzy.remake.core.OriginalObjectCatalog

fun main() {
    val all = OriginalObjectCatalog.all
    check(all.size == 61)
    check(all.all { it.areaId in 0..48 })
    check(all.any { it.areaId == 0 && it.x == 100 && it.y == 40 })
    check(all.any { it.areaId == 48 && it.x == 190 && it.y == 64 })
    check(OriginalObjectCatalog.inArea(19).size == 3)
    check(OriginalObjectCatalog.inArea(20).size == 3)
    check(OriginalObjectCatalog.inArea(49).isEmpty())
    println("original object catalog self-test OK")
}
