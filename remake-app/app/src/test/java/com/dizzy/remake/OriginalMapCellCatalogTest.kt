package com.dizzy.remake

import com.dizzy.remake.core.OriginalLocationCatalog
import com.dizzy.remake.core.OriginalMapCellCatalog as M
import org.junit.Assert.*
import org.junit.Test

class OriginalMapCellCatalogTest {
    @Test fun allRomBank2MapCellsAreEmbeddedRowMajor() {
        assertEquals(50,M.LOCATION_COUNT)
        assertEquals(6,M.ROWS)
        assertEquals(12018,M.TOTAL_CELL_BYTES)
        assertEquals(12018,M.totalCellCount())
        for(key in 0 until 50) {
            assertEquals(OriginalLocationCatalog[key].widthColumns32,M.widthColumns(key))
            assertEquals(M.widthColumns(key)*6,M.allCells(key).size)
        }

        assertEquals(15,M.widthColumns(0))
        assertEquals(listOf(95,27,39,27,39,27,39,27,39,27,39,27,39,27,96),M.row(0,0))
        assertEquals(listOf(63,28,29,28,29,28,47,3),M.row(0,1).take(8))
        assertEquals(listOf(31,18,19,16,17,51,99,57),M.row(0,5).takeLast(8))

        assertEquals(12,M.widthColumns(1))
        assertEquals(listOf(63,39,27,27,39,27,27,39,27,27,39,55),M.row(1,0))
        assertEquals(listOf(58,28,29,47,3,4,28,29),M.row(1,1).take(8))

        assertEquals(8,M.widthColumns(16))
        assertEquals((0..7).toList(),M.row(16,0))
        assertEquals((8..15).toList(),M.row(16,1))
        assertEquals((40..47).toList(),M.row(16,5))
        assertEquals((0..47).toList(),M.allCells(16))

        assertEquals(96,M.widthColumns(18))
        assertEquals(listOf(76,0,0,0,2,3,58,59,0,0,1,0),M.row(18,0).take(12))
        assertEquals(listOf(24,56,55,0,0,0,50,0),M.row(18,1).take(8))
        assertEquals(listOf(17,19,17,18,16,19,17,18),M.row(18,5).takeLast(8))

        assertEquals(80,M.widthColumns(29))
        assertEquals(listOf(9,10,0,0,23,11,12,84,85,92,94,93),M.row(29,0).take(12))
        assertEquals(listOf(32,32,32,46,47,0,0,0),M.row(29,5).takeLast(8))

        assertEquals(96,M.widthColumns(31))
        assertEquals(listOf(0,0,0,0,0,13,14,15,0,0,0,5),M.row(31,0).take(12))
        assertEquals(listOf(143,142,143,142,143,143,143,143),M.row(31,5).takeLast(8))

        assertEquals(40,M.widthColumns(34))
        assertEquals(listOf(81,81,81,81,81,81,81,81,81,86,87,88),M.row(34,0).take(12))

        assertEquals(32,M.widthColumns(44))
        assertEquals(listOf(79,116,116,116,116,116,116,116,116,116,61,65),M.row(44,0).take(12))

        assertEquals(16,M.widthColumns(49))
        assertEquals(listOf(1,1,2,28,30,29,0,26,31,27,1,3,2,3,12,14),M.row(49,0))
        assertEquals(listOf(24,25,22,25,12,13,32,32),M.row(49,5).takeLast(8))

        assertEquals(95,M.cellId(0,0,0))
        assertEquals(96,M.cellId(0,0,14))
        assertEquals(47,M.cellId(16,5,7))
        assertEquals(76,M.cellId(18,0,0))
        assertEquals(14,M.cellId(49,0,15))

        var bad=false
        try { M.cellId(0,6,0) } catch (_:IllegalArgumentException) { bad=true }
        assertTrue(bad)
        bad=false
        try { M.cellId(0,0,15) } catch (_:IllegalArgumentException) { bad=true }
        assertTrue(bad)
    }
}
