package com.dizzy.remake.core

data class OriginalObjectBehaviorDescriptor(
    val index:Int,
    val handlerCpu:Int,
    val sourceObjectCount:Int
)

/**
 * Exact 32-entry behavior-dispatch table used by bank-0 code at $8C61.
 * Record byte 6 is doubled and indexes the pointer table at $8C80.
 */
object OriginalObjectBehaviorCatalog {
    private val handlerCpu=intArrayOf(
        0x8C72,0x8CC3,0x8D35,0xA027,0xA22B,0x8E0C,0x96E4,0x90BA,
        0x9B69,0xA7A3,0x90F9,0x912A,0x94F8,0x95EB,0x9179,0x8E7E,
        0x904B,0x9087,0x9537,0x91C9,0x9417,0x9258,0xAACA,0xABED,
        0x9C80,0x9D29,0xACAE,0xAF73,0xAD66,0xAFF9,0xB06B,0xBAC3
    )
    private val sourceCounts=intArrayOf(
        18,8,3,5,1,1,1,1,1,1,1,1,1,1,1,1,
        1,1,1,2,1,1,2,1,1,1,1,1,1,1,1,1
    )

    val all:List<OriginalObjectBehaviorDescriptor> =
        (0 until 32).map { i -> OriginalObjectBehaviorDescriptor(i,handlerCpu[i],sourceCounts[i]) }

    init {
        require(handlerCpu.size==32)
        require(sourceCounts.size==32)
        require(sourceCounts.sum()==OriginalObjectCatalog.RECORD_COUNT)
        require(OriginalObjectCatalog.all.map{it.behaviorIndex}.toSet()==(0..31).toSet())
        require((0..31).all { i -> OriginalObjectCatalog.all.count{it.behaviorIndex==i}==sourceCounts[i] })
    }

    operator fun get(index:Int):OriginalObjectBehaviorDescriptor {
        require(index in 0..31)
        return all[index]
    }
}
