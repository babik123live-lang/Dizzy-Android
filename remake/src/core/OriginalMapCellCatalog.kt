package com.dizzy.remake.core

import java.io.ByteArrayOutputStream
import java.util.zip.Inflater

/**
 * Exact six-row, row-major map-cell grids extracted from ROM bank 2.
 *
 * Original routine $C903 starts at the current column and adds location width
 * ($4B) after every row, proving layout: index = row * width + column.
 */
object OriginalMapCellCatalog {
    private const val ENCODED =
        "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"

    const val LOCATION_COUNT = 50
    const val ROWS = 6
    const val TOTAL_CELL_BYTES = 12018

    private val maps: Array<ByteArray> by lazy { decodeMaps() }

    fun widthColumns(locationKey:Int):Int {
        require(locationKey in 0 until LOCATION_COUNT)
        return maps[locationKey].size / ROWS
    }

    fun cellId(locationKey:Int,row:Int,column:Int):Int {
        require(locationKey in 0 until LOCATION_COUNT)
        require(row in 0 until ROWS)
        val width=widthColumns(locationKey)
        require(column in 0 until width)
        return maps[locationKey][row*width+column].toInt() and 0xff
    }

    fun row(locationKey:Int,row:Int):List<Int> {
        val width=widthColumns(locationKey)
        return (0 until width).map { cellId(locationKey,row,it) }
    }

    fun allCells(locationKey:Int):List<Int> {
        require(locationKey in 0 until LOCATION_COUNT)
        return maps[locationKey].map { it.toInt() and 0xff }
    }

    fun totalCellCount():Int = maps.sumOf { it.size }

    private fun decodeMaps():Array<ByteArray> {
        val packed=inflate(decodeBase64(ENCODED))
        require(packed.size==12074)
        require(packed[0].toInt()==0x44 && packed[1].toInt()==0x5A &&
                packed[2].toInt()==0x4D && packed[3].toInt()==0x43)
        require((packed[4].toInt() and 0xff)==1)
        require((packed[5].toInt() and 0xff)==LOCATION_COUNT)
        var p=6
        val result=Array(LOCATION_COUNT){ByteArray(0)}
        for(key in 0 until LOCATION_COUNT) {
            val width=packed[p++].toInt() and 0xff
            require(width==OriginalLocationCatalog[key].widthColumns32)
            val size=width*ROWS
            result[key]=packed.copyOfRange(p,p+size)
            p+=size
        }
        require(p==packed.size)
        require(result.sumOf{it.size}==TOTAL_CELL_BYTES)
        return result
    }

    private fun inflate(compressed:ByteArray):ByteArray {
        val inflater=Inflater()
        inflater.setInput(compressed)
        val out=ByteArrayOutputStream(TOTAL_CELL_BYTES+128)
        val buffer=ByteArray(4096)
        while(!inflater.finished()) {
            val n=inflater.inflate(buffer)
            require(n>0 || !inflater.needsInput())
            if(n>0) out.write(buffer,0,n)
        }
        inflater.end()
        return out.toByteArray()
    }

    private fun decodeBase64(s:String):ByteArray {
        val alphabet="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
        require(s.length%4==0)
        val padding=(if(s.endsWith("==")) 2 else if(s.endsWith("=")) 1 else 0)
        val out=ByteArray(s.length/4*3-padding)
        var oi=0
        var i=0
        while(i<s.length) {
            val a=alphabet.indexOf(s[i])
            val b=alphabet.indexOf(s[i+1])
            val c=if(s[i+2]=='=') 0 else alphabet.indexOf(s[i+2])
            val d=if(s[i+3]=='=') 0 else alphabet.indexOf(s[i+3])
            require(a>=0 && b>=0 && c>=0 && d>=0)
            val v=(a shl 18) or (b shl 12) or (c shl 6) or d
            if(oi<out.size) out[oi++]=(v ushr 16).toByte()
            if(oi<out.size) out[oi++]=(v ushr 8).toByte()
            if(oi<out.size) out[oi++]=v.toByte()
            i+=4
        }
        require(oi==out.size)
        return out
    }
}
