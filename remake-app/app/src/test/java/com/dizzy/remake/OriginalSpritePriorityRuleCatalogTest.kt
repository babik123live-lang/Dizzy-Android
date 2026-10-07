package com.dizzy.remake

import com.dizzy.remake.core.OriginalPriorityRuleDirection
import com.dizzy.remake.core.OriginalSpritePriorityRuleCatalog
import org.junit.Assert.*
import org.junit.Test

class OriginalSpritePriorityRuleCatalogTest {
    @Test fun rulesMatchExtractedF46fAndF492Streams() {
        val all=OriginalSpritePriorityRuleCatalog.all
        assertEquals(26,all.size)
        assertEquals(23,all.count{it.direction==OriginalPriorityRuleDirection.BELOW_THRESHOLD})
        assertEquals(3,all.count{it.direction==OriginalPriorityRuleDirection.AT_OR_ABOVE_THRESHOLD})
        assertEquals(setOf(3,10,46,86,208,214),all.filter{it.direction==OriginalPriorityRuleDirection.BELOW_THRESHOLD}.map{it.thresholdRaw}.toSet())
        assertEquals(setOf(115),all.filter{it.direction==OriginalPriorityRuleDirection.AT_OR_ABOVE_THRESHOLD}.map{it.thresholdRaw}.toSet())
        assertEquals(1,OriginalSpritePriorityRuleCatalog.forLocation(8).size)
        assertEquals(1,OriginalSpritePriorityRuleCatalog.forLocation(15).size)
        assertEquals(1,OriginalSpritePriorityRuleCatalog.forLocation(45).size)
        assertEquals(1,OriginalSpritePriorityRuleCatalog.forLocation(46).size)
        assertEquals(1,OriginalSpritePriorityRuleCatalog.forLocation(47).size)
        assertTrue(OriginalSpritePriorityRuleCatalog.forLocation(0).isEmpty())
        assertTrue(all.all{it.thresholdRaw in 0..255})
        assertTrue(all.all{it.locationKey in 0 until 50})
    }
}
