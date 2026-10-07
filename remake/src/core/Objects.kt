package com.dizzy.remake.core

enum class ObjectKind { ITEM, HAZARD, NPC, EXIT, PLATFORM, DECORATION }
data class WorldObject(
    val id:Int, val kind:ObjectKind, var x:Float, var y:Float,
    val width:Float=16f, val height:Float=16f, var active:Boolean=true,
    val properties:MutableMap<String,Int> = mutableMapOf()
) { fun box()=RectF(x,y,width,height) }

data class Exit(val targetRoom:Int,val trigger:RectF,val targetX:Float,val targetY:Float)

class RoomGraph(private val rooms:Map<Int,Room>, private val exits:Map<Int,List<Exit>>) {
    fun room(id:Int)=rooms[id] ?: error("Unknown room $id")
    fun exitAt(roomId:Int,player:RectF)=exits[roomId].orEmpty().firstOrNull{it.trigger.intersects(player)}
}
