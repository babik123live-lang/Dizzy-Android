package com.dizzy.remake.tests

import com.dizzy.remake.core.OriginalObjectCatalog

fun main() {
    val all = OriginalObjectCatalog.all
    check(com.dizzy.remake.core.OriginalObject.BYTE_SIZE == 10)
    check(OriginalObjectCatalog.RECORD_COUNT == 62)
    check(OriginalObjectCatalog.TABLE_START_PRG_OFFSET == 259428)
    check(OriginalObjectCatalog.TABLE_END_EXCLUSIVE_PRG_OFFSET == 260048)
    check(all.size == 62)
    check(all[45].raw0 == 49 && all[45].raw1 == 240 && all[45].raw3 == 60)
    check(all[46].raw0 == 5 && all[46].raw1 == 148)
    check(all.last().raw0 == 19 && all.last().raw1 == 172)
    check(all.all { it.raw0 in 0..49 })
    check(all.any { it.raw0 == 0 && it.raw1 == 100 && it.raw3 == 40 })
    check(all.any { it.raw0 == 48 && it.raw1 == 190 && it.raw3 == 64 })
    check(all.any { it.raw0 == 49 && it.raw1 == 240 && it.raw3 == 60 && it.raw45le == 35912 && it.raw78le == 44293 })
    check(OriginalObjectCatalog.inArea(19).size == 3)
    check(OriginalObjectCatalog.inArea(20).size == 3)
    check(OriginalObjectCatalog.inArea(49).size == 1)
    check(OriginalObjectCatalog.inArea(50).isEmpty())
    println("original object catalog self-test OK")
}
