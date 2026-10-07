package com.dizzy.remake.tests

import com.dizzy.remake.core.OriginalObjectCatalog

fun main() {
    val all = OriginalObjectCatalog.all
    check(all.size == 62)
    check(all.all { it.raw0 in 0..49 })
    check(all.any { it.raw0 == 0 && it.x == 100 && it.y == 40 })
    check(all.any { it.raw0 == 48 && it.x == 190 && it.y == 64 })
    check(all.any { it.raw0 == 49 && it.x == 240 && it.y == 60 && it.spriteAddress == 35912 && it.descriptionAddress == 44293 })
    check(OriginalObjectCatalog.inArea(19).size == 3)
    check(OriginalObjectCatalog.inArea(20).size == 3)
    check(OriginalObjectCatalog.inArea(49).size == 1)
    check(OriginalObjectCatalog.inArea(50).isEmpty())
    println("original object catalog self-test OK")
}
