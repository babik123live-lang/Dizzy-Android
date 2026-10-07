package com.dizzy.remake.core

enum class OriginalPriorityRuleDirection { BELOW_THRESHOLD, AT_OR_ABOVE_THRESHOLD }

data class OriginalSpritePriorityRule(
    val locationKey:Int,
    val scriptOpIndex:Int,
    val thresholdRaw:Int,
    val direction:OriginalPriorityRuleDirection
)

/**
 * Rules consumed by $F46F/$F492. Those handlers select bank 8 and scan OAM;
 * qualifying sprites get NES attribute bit $20 (behind background) set.
 */
object OriginalSpritePriorityRuleCatalog {
    val all=listOf(
        OriginalSpritePriorityRule(8,3,208,OriginalPriorityRuleDirection.BELOW_THRESHOLD),
        OriginalSpritePriorityRule(9,5,208,OriginalPriorityRuleDirection.BELOW_THRESHOLD),
        OriginalSpritePriorityRule(10,3,208,OriginalPriorityRuleDirection.BELOW_THRESHOLD),
        OriginalSpritePriorityRule(11,7,208,OriginalPriorityRuleDirection.BELOW_THRESHOLD),
        OriginalSpritePriorityRule(12,7,208,OriginalPriorityRuleDirection.BELOW_THRESHOLD),
        OriginalSpritePriorityRule(13,7,208,OriginalPriorityRuleDirection.BELOW_THRESHOLD),
        OriginalSpritePriorityRule(14,8,208,OriginalPriorityRuleDirection.BELOW_THRESHOLD),
        OriginalSpritePriorityRule(15,1,214,OriginalPriorityRuleDirection.BELOW_THRESHOLD),
        OriginalSpritePriorityRule(23,1,86,OriginalPriorityRuleDirection.BELOW_THRESHOLD),
        OriginalSpritePriorityRule(32,2,46,OriginalPriorityRuleDirection.BELOW_THRESHOLD),
        OriginalSpritePriorityRule(33,2,46,OriginalPriorityRuleDirection.BELOW_THRESHOLD),
        OriginalSpritePriorityRule(34,1,10,OriginalPriorityRuleDirection.BELOW_THRESHOLD),
        OriginalSpritePriorityRule(35,2,10,OriginalPriorityRuleDirection.BELOW_THRESHOLD),
        OriginalSpritePriorityRule(36,2,10,OriginalPriorityRuleDirection.BELOW_THRESHOLD),
        OriginalSpritePriorityRule(37,3,3,OriginalPriorityRuleDirection.BELOW_THRESHOLD),
        OriginalSpritePriorityRule(38,3,3,OriginalPriorityRuleDirection.BELOW_THRESHOLD),
        OriginalSpritePriorityRule(39,3,3,OriginalPriorityRuleDirection.BELOW_THRESHOLD),
        OriginalSpritePriorityRule(40,3,3,OriginalPriorityRuleDirection.BELOW_THRESHOLD),
        OriginalSpritePriorityRule(41,3,3,OriginalPriorityRuleDirection.BELOW_THRESHOLD),
        OriginalSpritePriorityRule(42,3,3,OriginalPriorityRuleDirection.BELOW_THRESHOLD),
        OriginalSpritePriorityRule(43,4,3,OriginalPriorityRuleDirection.BELOW_THRESHOLD),
        OriginalSpritePriorityRule(44,1,10,OriginalPriorityRuleDirection.BELOW_THRESHOLD),
        OriginalSpritePriorityRule(48,1,10,OriginalPriorityRuleDirection.BELOW_THRESHOLD),
        OriginalSpritePriorityRule(45,2,115,OriginalPriorityRuleDirection.AT_OR_ABOVE_THRESHOLD),
        OriginalSpritePriorityRule(46,2,115,OriginalPriorityRuleDirection.AT_OR_ABOVE_THRESHOLD),
        OriginalSpritePriorityRule(47,2,115,OriginalPriorityRuleDirection.AT_OR_ABOVE_THRESHOLD)
    )

    init {
        require(all.size==26)
        require(all.count{it.direction==OriginalPriorityRuleDirection.BELOW_THRESHOLD}==23)
        require(all.count{it.direction==OriginalPriorityRuleDirection.AT_OR_ABOVE_THRESHOLD}==3)
        require(all.all{it.locationKey in 0 until OriginalLocationCatalog.COUNT})
        require(all.all{it.thresholdRaw in 0..255})
    }

    fun forLocation(key:Int)=all.filter{it.locationKey==key}
}
