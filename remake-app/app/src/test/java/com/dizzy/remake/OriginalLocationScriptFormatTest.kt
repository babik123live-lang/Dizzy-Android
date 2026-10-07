package com.dizzy.remake

import com.dizzy.remake.core.OriginalLocationScriptFormat as F
import org.junit.Assert.*
import org.junit.Test

class OriginalLocationScriptFormatTest {
    @Test fun dispatcherContractMatchesReverseEngineeredStreamFormat() {
        assertEquals(0xF439,F.DISPATCHER_CPU)
        assertEquals(1,F.SCRIPT_BANK)
        assertEquals(0,F.TERMINATOR_HIGH_BYTE)
        assertEquals(3,F.immediateArgumentCount(0xF498))
        assertEquals(1,F.immediateArgumentCount(0xF46F))
        assertEquals(1,F.immediateArgumentCount(0xF492))
        assertEquals(5,F.immediateArgumentCount(0xF4C1))
        assertEquals(0,F.immediateArgumentCount(0x896D))
        assertTrue(F.isKnownArgumentConsumer(0xF498))
        assertTrue(F.isKnownArgumentConsumer(0xF4C1))
        assertFalse(F.isKnownArgumentConsumer(0x896D))
    }
}
