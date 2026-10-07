package com.dizzy.remake.tests

import com.dizzy.remake.core.OriginalObjectCatalog

fun main() {
    val all = OriginalObjectCatalog.all
    check(com.dizzy.remake.core.OriginalObject.BYTE_SIZE == 10)
    check(OriginalObjectCatalog.RECORD_COUNT == 64)
    check(OriginalObjectCatalog.TABLE_START_PRG_OFFSET == 259408)
    check(OriginalObjectCatalog.TABLE_END_EXCLUSIVE_PRG_OFFSET == 260048)
    check(OriginalObjectCatalog.TABLE_END_EXCLUSIVE_PRG_OFFSET - OriginalObjectCatalog.TABLE_START_PRG_OFFSET == 640)
    check(OriginalObjectCatalog.RECORD_COUNT * com.dizzy.remake.core.OriginalObject.BYTE_SIZE == 640)
    check(all.size == 64)
    check(all.map { it.raw6 }.toSet() == (0..31).toSet())
    check(all.map { it.raw9 }.toSet() == (0..7).toSet())
    check(all.all { (it.raw45le ushr 8) in 0x80..0xAA })
    check(all.all { (it.raw78le ushr 8) in 0xAC..0xBD })
    check(all[0].raw0 == 255 && all[0].raw1 == 50 && all[0].raw45le == 32772)
    check(all[1] == all[0])
    check(all[47].raw0 == 49 && all[47].raw1 == 240 && all[47].raw3 == 60)
    check(all[48].raw0 == 5 && all[48].raw1 == 148)
    check(all.last().raw0 == 19 && all.last().raw1 == 172)
    check(all.any { it.raw0 == 0 && it.raw1 == 100 && it.raw3 == 40 })
    check(all.any { it.raw0 == 48 && it.raw1 == 190 && it.raw3 == 64 })
    check(all.any { it.raw0 == 49 && it.raw1 == 240 && it.raw3 == 60 && it.raw45le == 35912 && it.raw78le == 44293 })
    check(OriginalObjectCatalog.byRaw0(19).size == 3)
    check(OriginalObjectCatalog.byRaw0(20).size == 3)
    check(OriginalObjectCatalog.byRaw0(49).size == 1)
    check(OriginalObjectCatalog.byRaw0(50).isEmpty())
    println("original object catalog self-test OK")
}
