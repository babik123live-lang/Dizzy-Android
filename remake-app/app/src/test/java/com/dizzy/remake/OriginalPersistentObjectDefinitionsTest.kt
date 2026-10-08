package com.dizzy.remake

import com.dizzy.remake.core.OriginalPersistentObjectDefinitions
import org.junit.Assert.*
import org.junit.Test

class OriginalPersistentObjectDefinitionsTest {
    @Test fun joinedDefinitionsStaySourceIndexed() {
        val all=OriginalPersistentObjectDefinitions.all
        assertEquals(64,all.size)
        assertEquals((0 until 64).toList(),all.map{it.sourceIndex})
        assertTrue(all.all{it.identity.sourceIndex==it.sourceIndex})
        assertTrue(all.all{it.behavior.index==it.record.behaviorIndex})
        assertEquals(1,OriginalPersistentObjectDefinitions.forLocationKey(0).size)
        assertEquals(3,OriginalPersistentObjectDefinitions.forLocationKey(19).size)
        assertEquals(3,OriginalPersistentObjectDefinitions.forLocationKey(20).size)
        assertEquals(1,OriginalPersistentObjectDefinitions.forLocationKey(49).size)
        assertEquals(2,OriginalPersistentObjectDefinitions.forLocationKey(255).size)
        assertEquals("DIZZY'S DOOR KEY",OriginalPersistentObjectDefinitions[9].identity.descriptionEn)
        assertEquals(0x8CC3,OriginalPersistentObjectDefinitions[9].behavior.handlerCpu)
        assertEquals("A MACHINE WRENCH",OriginalPersistentObjectDefinitions[2].identity.descriptionEn)
        assertEquals(0xBAC3,OriginalPersistentObjectDefinitions[2].behavior.handlerCpu)
        assertEquals(50,OriginalPersistentObjectDefinitions[0].record.worldX)
        assertEquals(40,OriginalPersistentObjectDefinitions[0].record.spriteY)
        var bad=false
        try { OriginalPersistentObjectDefinitions[64] } catch (_:IllegalArgumentException) { bad=true }
        assertTrue(bad)
    }
}
