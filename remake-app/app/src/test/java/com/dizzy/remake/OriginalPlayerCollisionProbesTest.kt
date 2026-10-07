package com.dizzy.remake

import com.dizzy.remake.core.OriginalPlayerCollisionProbes as P
import org.junit.Assert.*
import org.junit.Test

class OriginalPlayerCollisionProbesTest {
    @Test fun offsetsMatchBankZeroCollisionCalls() {
        assertEquals(8,P.all.size)
        assertEquals(listOf(-4,4),P.verticalSnap.map{it.dx})
        assertEquals(listOf(-1,-1),P.verticalSnap.map{it.dy})
        assertEquals(listOf(-8,8),P.sideMotion.map{it.dx})
        assertEquals(listOf(-12,-12),P.sideMotion.map{it.dy})
        assertEquals(listOf(-4,4),P.upperMotion.map{it.dx})
        assertEquals(listOf(-26,-26),P.upperMotion.map{it.dy})
        assertEquals(listOf(-4,4),P.lowerMotion.map{it.dx})
        assertEquals(listOf(0,0),P.lowerMotion.map{it.dy})
        assertEquals(listOf(0x834B,0x8359),P.verticalSnap.map{it.sourceCpu})
        assertEquals(listOf(0x8543,0x8543),P.sideMotion.map{it.sourceCpu})
        assertEquals(listOf(0x857D,0x8591),P.upperMotion.map{it.sourceCpu})
        assertEquals(listOf(0x85C0,0x85CD),P.lowerMotion.map{it.sourceCpu})
        assertEquals(-8,P.all.minOf{it.dx})
        assertEquals(8,P.all.maxOf{it.dx})
        assertEquals(-26,P.all.minOf{it.dy})
        assertEquals(0,P.all.maxOf{it.dy})
    }
}
