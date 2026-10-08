package com.dizzy.remake

import com.dizzy.remake.core.OriginalObjectCatalog
import com.dizzy.remake.core.OriginalObjectIdentityCatalog
import org.junit.Assert.*
import org.junit.Test

class OriginalObjectIdentityCatalogTest {
    @Test fun identitiesMatchOriginalBank9InventoryRecords() {
        val all=OriginalObjectIdentityCatalog.all
        assertEquals(64,all.size)
        assertEquals((0 until 64).toList(),all.map{it.sourceIndex})
        assertTrue(all.indices.all{i->all[i].inventoryRecordCpu==OriginalObjectCatalog.all[i].inventoryRecordCpu})
        assertTrue(all.all{it.inventoryRecordCpu in 0x8000..0xBFFF})
        assertTrue(all.all{it.inventoryGraphicsCpu in 0x8000..0xBFFF})
        assertEquals(55,all.map{it.inventoryRecordCpu}.toSet().size)
        assertEquals(46,all.map{it.inventoryGraphicsCpu}.toSet().size)
        assertEquals(8,all.count{it.descriptionEn=="NOTHING"})
        assertEquals("NOTHING",all[0].descriptionEn)
        assertEquals("A MACHINE WRENCH",all[2].descriptionEn)
        assertEquals("DIZZY'S DOOR KEY",all[9].descriptionEn)
        assertEquals("A ONE TON WEIGHT",all[8].descriptionEn)
        assertEquals("A LARGE GOLD COIN",all[47].descriptionEn)
        assertEquals("A BARREL OF\nPIRATES RUM",all[63].descriptionEn)
        assertEquals(0x8649,all[9].inventoryGraphicsCpu)
        assertEquals(0x8649,all[13].inventoryGraphicsCpu)
        assertEquals(0x8583,all[10].inventoryGraphicsCpu)
        assertEquals(0x8583,all[59].inventoryGraphicsCpu)
        assertEquals(all[48].inventoryRecordCpu,all[49].inventoryRecordCpu)
        assertEquals(all[51].inventoryRecordCpu,all[52].inventoryRecordCpu)
        assertEquals("A WARM GOLDEN\nDRAGON EGG",all[48].descriptionEn)
        assertEquals("A CAGE CONTAINING\nPOGIE THE FLUFFLE",all[51].descriptionEn)
    }
}
