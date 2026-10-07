package com.dizzy.remake

import com.dizzy.remake.core.StarCatalog
import com.dizzy.remake.core.StarLayout
import com.dizzy.remake.core.StarSpriteVariant
import org.junit.Assert.*
import org.junit.Test

class StarCatalogTest {
    @Test fun romDerivedCatalogIsInternallyConsistent() {
        val all=StarCatalog.all
        assertEquals(250,StarCatalog.TOTAL)
        assertEquals(250,StarCatalog.COUNTER_INITIAL)
        assertEquals(0x0772,StarCatalog.COUNTER_RAM)
        assertEquals(0x0751,StarCatalog.PACKED_STATE_FIRST_RAM)
        assertEquals(0x0770,StarCatalog.PACKED_STATE_LAST_RAM)
        assertEquals(250,all.size)
        assertEquals((0 until 250).toList(),all.map{it.id})
        assertEquals(250,all.map{it.id}.toSet().size)

        val main=all.filter{it.layout==StarLayout.HORIZONTAL_GROUPED}
        assertEquals(210,main.size)
        assertTrue(main.all{it.locationKeyRaw!=null})
        val keys=main.mapNotNull{it.locationKeyRaw}
        assertEquals(keys.sorted(),keys)
        assertEquals(0,keys.minOrNull())
        assertEquals(49,keys.maxOrNull())
        assertEquals(49,keys.toSet().size)
        assertFalse(16 in keys.toSet())
        assertEquals(38,main.minOf{it.worldAxis})
        assertEquals(2828,main.maxOf{it.worldAxis})
        assertEquals(12,main.minOf{it.fixedAxis})
        assertEquals(161,main.maxOf{it.fixedAxis})
        assertTrue(main.all{it.spriteVariant==StarSpriteVariant.MAIN_4F})

        val va=all.filter{it.layout==StarLayout.VERTICAL_A}
        val vb=all.filter{it.layout==StarLayout.VERTICAL_B}
        val vc=all.filter{it.layout==StarLayout.VERTICAL_C}
        assertEquals(10,va.size)
        assertEquals(20,vb.size)
        assertEquals(10,vc.size)
        assertEquals((210 until 220).toList(),va.map{it.id})
        assertEquals((220 until 240).toList(),vb.map{it.id})
        assertEquals((240 until 250).toList(),vc.map{it.id})
        assertTrue(va.all{it.locationKeyRaw==null && it.spriteVariant==StarSpriteVariant.MAIN_4F})
        assertTrue(vb.all{it.locationKeyRaw==null && it.spriteVariant==StarSpriteVariant.ALT_F0})
        assertTrue(vc.all{it.locationKeyRaw==null && it.spriteVariant==StarSpriteVariant.MAIN_4F})
        assertEquals(2,StarCatalog.horizontalForLocationKey(0).size)
        assertTrue(StarCatalog.horizontalForLocationKey(16).isEmpty())
        assertEquals(3,StarCatalog.horizontalForLocationKey(49).size)
        assertEquals(352,all.first().worldAxis)
        assertEquals(81,all.first().fixedAxis)
        assertEquals(459,all[209].worldAxis)
        assertEquals(87,all[209].fixedAxis)
        assertEquals(448,all[210].worldAxis)
        assertEquals(176,all[210].fixedAxis)
        assertEquals(4352,all.last().worldAxis)
        assertEquals(128,all.last().fixedAxis)
    }
}
