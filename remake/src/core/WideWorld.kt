package com.dizzy.remake.core

data class StripRoom(val roomId:Int,val originX:Float,val width:Float)
data class WorldStrip(val region:Region,val rooms:List<StripRoom>) {
    val width:Float get()=rooms.maxOfOrNull{it.originX+it.width} ?: 0f
    fun localToWorld(roomId:Int,x:Float):Float {
        val room=rooms.first{it.roomId==roomId}
        return room.originX+x
    }
}

/** Builds wide horizontal strips only after room ordering has been verified. */
class WideWorldBuilder {
    fun build(region:Region, orderedRoomIds:List<Int>, widths:Map<Int,Float>):WorldStrip {
        var x=0f
        val rooms=orderedRoomIds.map { id ->
            val w=widths[id] ?: error("Missing verified width for room $id")
            require(w>=256f)
            StripRoom(id,x,w).also{x+=w}
        }
        return WorldStrip(region,rooms)
    }
}
