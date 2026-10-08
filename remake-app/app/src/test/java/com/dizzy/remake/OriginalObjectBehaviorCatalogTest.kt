package com.dizzy.remake

import com.dizzy.remake.core.OriginalObjectBehaviorCatalog
import org.junit.Assert.*
import org.junit.Test

class OriginalObjectBehaviorCatalogTest {
    @Test fun dispatchTableMatchesOriginal8c80Table() {
        val all=OriginalObjectBehaviorCatalog.all
        assertEquals(32,all.size)
        assertEquals((0 until 32).toList(),all.map{it.index})
        assertEquals(64,all.sumOf{it.sourceObjectCount})
        assertEquals(0x8C72,all[0].handlerCpu)
        assertEquals(0x8CC3,all[1].handlerCpu)
        assertEquals(0x8D35,all[2].handlerCpu)
        assertEquals(0xA027,all[3].handlerCpu)
        assertEquals(0xBAC3,all[31].handlerCpu)
        assertEquals(18,all[0].sourceObjectCount)
        assertEquals(8,all[1].sourceObjectCount)
        assertEquals(3,all[2].sourceObjectCount)
        assertEquals(5,all[3].sourceObjectCount)
        assertEquals(2,all[19].sourceObjectCount)
        assertEquals(2,all[22].sourceObjectCount)
        assertTrue((4..18).filter{it!=19}.all{all[it].sourceObjectCount>=1})
        assertEquals(OriginalObjectBehaviorCatalog[0],all[0])
        assertEquals(OriginalObjectBehaviorCatalog[31],all[31])
        var bad=false
        try { OriginalObjectBehaviorCatalog[32] } catch (_:IllegalArgumentException) { bad=true }
        assertTrue(bad)
    }
}
