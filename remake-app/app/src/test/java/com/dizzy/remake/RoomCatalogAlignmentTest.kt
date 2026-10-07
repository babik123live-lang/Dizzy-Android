package com.dizzy.remake

import com.dizzy.remake.core.OriginalLocationCatalog
import com.dizzy.remake.core.Region
import com.dizzy.remake.core.RoomCatalog
import org.junit.Assert.*
import org.junit.Test

class RoomCatalogAlignmentTest {
    @Test fun labelsStayAlignedToOriginalRuntimeKeys() {
        val rooms=RoomCatalog.rooms
        assertEquals(OriginalLocationCatalog.COUNT,rooms.size)
        assertEquals((0 until 50).toList(),rooms.map{it.textId})
        assertEquals("Dom Dizzy'ego",rooms[0].namePl)
        assertEquals("Kopalnia diamentów – windy",rooms[16].namePl)
        assertEquals("Stary drewniany most",rooms[17].namePl)
        assertEquals("Carber Bay",rooms[18].namePl)
        assertEquals("Crystal Falls",rooms[19].namePl)
        assertEquals("Wrak pirackiego galeonu",rooms[22].namePl)
        assertEquals("Castle Street",rooms[29].namePl)
        assertEquals("Dock Street",rooms[31].namePl)
        assertEquals("Hol zamku trolli",rooms[44].namePl)
        assertEquals("Jaskinia pod dnem oceanu",rooms[49].namePl)
        assertEquals(Region.MINES,rooms[16].region)
        assertEquals(Region.COUNTRYSIDE,rooms[17].region)
        assertEquals(Region.TROLL_CASTLE,rooms[44].region)
        assertEquals(Region.OCEAN_CAVE,rooms[49].region)
    }
}
