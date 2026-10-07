package com.dizzy.remake

import com.dizzy.remake.core.OriginalGameStart
import org.junit.Assert.assertEquals
import org.junit.Test

class OriginalGameStartTest {
    @Test fun startupConstantsMatchOriginalBankZeroCode() {
        assertEquals(0, OriginalGameStart.LOCATION_KEY)
        assertEquals(160, OriginalGameStart.WORLD_X)
        assertEquals(176, OriginalGameStart.RAW_A3)
    }
}
