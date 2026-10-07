package com.dizzy.remake.core

data class RectF(val x:Float,val y:Float,val w:Float,val h:Float) {
    fun intersects(o:RectF)=x<o.x+o.w && x+w>o.x && y<o.y+o.h && y+h>o.y
}
data class Solid(val box:RectF)
data class Room(val id:Int,val width:Float,val height:Float=240f,val solids:List<Solid> = emptyList())
data class Player(var x:Float,var y:Float,var vx:Float=0f,var vy:Float=0f,var grounded:Boolean=false)

class World(var room:Room, val player:Player) {
    companion object { const val GRAVITY=520f; const val MOVE=92f; const val JUMP=205f }
    fun tick(left:Boolean,right:Boolean,jump:Boolean,dt:Float=1f/50f) {
        player.vx = when { left&&!right -> -MOVE; right&&!left -> MOVE; else -> 0f }
        if(jump && player.grounded){ player.vy=-JUMP; player.grounded=false }
        player.vy=(player.vy+GRAVITY*dt).coerceAtMost(260f)
        moveX(player.vx*dt); moveY(player.vy*dt)
    }
    private fun box()=RectF(player.x,player.y,16f,24f)
    private fun moveX(dx:Float){ player.x+=dx; for(s in room.solids) if(box().intersects(s.box)){ player.x=if(dx>0)s.box.x-16f else s.box.x+s.box.w } }
    private fun moveY(dy:Float){ player.grounded=false; player.y+=dy; for(s in room.solids) if(box().intersects(s.box)){ if(dy>0){player.y=s.box.y-24f;player.grounded=true}else player.y=s.box.y+s.box.h; player.vy=0f } }
}
