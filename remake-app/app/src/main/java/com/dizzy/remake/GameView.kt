package com.dizzy.remake

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import com.dizzy.remake.core.*
import kotlin.math.sin

class GameView(context: Context) : View(context) {
    private val clock = FixedStepClock()
    private val input = InputState()
    private val scene = RemasterScenes.opening()
    private val world = World(scene.room, Player(128f, 150f))
    private val engine = Engine(world, input)
    private val viewport = Viewport()
    private val p = Paint(Paint.ANTI_ALIAS_FLAG)
    private var running = false
    private var t = 0f\n    private val audio = RemasterAudio()\n    private var lastA = false

    private val frame = object : Runnable {
        override fun run() {
            if (!running || !isAttachedToWindow) return
            clock.advance(System.nanoTime()) {
                engine.tick(); t += 1f / 50f
                handleRoomTransition()
            }
            invalidate(); postOnAnimation(this)
        }
    }

    override fun onAttachedToWindow() { super.onAttachedToWindow(); running=true; audio.start(); clock.reset(System.nanoTime()); postOnAnimation(frame) }
    override fun onDetachedFromWindow() { running=false; audio.stop(); removeCallbacks(frame); super.onDetachedFromWindow() }

    private fun handleRoomTransition() {
        if (currentRoom == 0 && world.player.x >= world.room.width - 18f) {
            currentRoom = 1
            scene = RemasterScenes.room(1)
            world.room = scene.room
            world.player.x = 18f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 1 && world.player.x >= world.room.width - 18f) {
            currentRoom = 2
            scene = RemasterScenes.room(2)
            world.room = scene.room
            world.player.x = 18f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 2 && world.player.x >= world.room.width - 18f) {
            currentRoom = 3
            scene = RemasterScenes.room(3)
            world.room = scene.room
            world.player.x = 18f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 3 && world.player.x >= world.room.width - 18f) {
            currentRoom = 4
            scene = RemasterScenes.room(4)
            world.room = scene.room
            world.player.x = 18f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 4 && world.player.x >= world.room.width - 18f) {
            currentRoom = 5
            scene = RemasterScenes.room(5)
            world.room = scene.room
            world.player.x = 18f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 5 && world.player.x >= world.room.width - 18f) {
            currentRoom = 6
            scene = RemasterScenes.room(6)
            world.room = scene.room
            world.player.x = 18f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 6 && world.player.x >= world.room.width - 18f) {
            currentRoom = 7
            scene = RemasterScenes.room(7)
            world.room = scene.room
            world.player.x = 18f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 7 && world.player.x >= world.room.width - 18f) {
            currentRoom = 8
            scene = RemasterScenes.room(8)
            world.room = scene.room
            world.player.x = 18f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 8 && world.player.x >= world.room.width - 18f) {
            currentRoom = 9
            scene = RemasterScenes.room(9)
            world.room = scene.room
            world.player.x = 18f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 9 && world.player.x >= world.room.width - 18f) {
            currentRoom = 10
            scene = RemasterScenes.room(10)
            world.room = scene.room
            world.player.x = 18f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 10 && world.player.x >= world.room.width - 18f) {
            currentRoom = 11
            scene = RemasterScenes.room(11)
            world.room = scene.room
            world.player.x = 18f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 11 && world.player.x >= world.room.width - 18f) {
            currentRoom = 12
            scene = RemasterScenes.room(12)
            world.room = scene.room
            world.player.x = 18f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 12 && world.player.x >= world.room.width - 18f) {
            currentRoom = 13
            scene = RemasterScenes.room(13)
            world.room = scene.room
            world.player.x = 18f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 13 && world.player.x >= world.room.width - 18f) {
            currentRoom = 14
            scene = RemasterScenes.room(14)
            world.room = scene.room
            world.player.x = 18f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 14 && world.player.x >= world.room.width - 18f) {
            currentRoom = 15
            scene = RemasterScenes.room(15)
            world.room = scene.room
            world.player.x = 18f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 15 && world.player.x >= world.room.width - 18f) {
            currentRoom = 16
            scene = RemasterScenes.room(16)
            world.room = scene.room
            world.player.x = 18f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 16 && world.player.x <= 2f) {
            currentRoom = 15
            scene = RemasterScenes.room(15)
            world.room = scene.room
            world.player.x = world.room.width - 38f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 15 && world.player.x <= 2f) {
            currentRoom = 14
            scene = RemasterScenes.room(14)
            world.room = scene.room
            world.player.x = world.room.width - 38f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 14 && world.player.x <= 2f) {
            currentRoom = 13
            scene = RemasterScenes.room(13)
            world.room = scene.room
            world.player.x = world.room.width - 38f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 13 && world.player.x <= 2f) {
            currentRoom = 12
            scene = RemasterScenes.room(12)
            world.room = scene.room
            world.player.x = world.room.width - 38f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 12 && world.player.x <= 2f) {
            currentRoom = 11
            scene = RemasterScenes.room(11)
            world.room = scene.room
            world.player.x = world.room.width - 38f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 11 && world.player.x <= 2f) {
            currentRoom = 10
            scene = RemasterScenes.room(10)
            world.room = scene.room
            world.player.x = world.room.width - 38f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 10 && world.player.x <= 2f) {
            currentRoom = 9
            scene = RemasterScenes.room(9)
            world.room = scene.room
            world.player.x = world.room.width - 38f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 9 && world.player.x <= 2f) {
            currentRoom = 8
            scene = RemasterScenes.room(8)
            world.room = scene.room
            world.player.x = world.room.width - 38f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 8 && world.player.x <= 2f) {
            currentRoom = 7
            scene = RemasterScenes.room(7)
            world.room = scene.room
            world.player.x = world.room.width - 38f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 7 && world.player.x <= 2f) {
            currentRoom = 6
            scene = RemasterScenes.room(6)
            world.room = scene.room
            world.player.x = world.room.width - 38f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 6 && world.player.x <= 2f) {
            currentRoom = 5
            scene = RemasterScenes.room(5)
            world.room = scene.room
            world.player.x = world.room.width - 38f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 5 && world.player.x <= 2f) {
            currentRoom = 4
            scene = RemasterScenes.room(4)
            world.room = scene.room
            world.player.x = world.room.width - 38f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 4 && world.player.x <= 2f) {
            currentRoom = 3
            scene = RemasterScenes.room(3)
            world.room = scene.room
            world.player.x = world.room.width - 38f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 3 && world.player.x <= 2f) {
            currentRoom = 2
            scene = RemasterScenes.room(2)
            world.room = scene.room
            world.player.x = world.room.width - 38f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 2 && world.player.x <= 2f) {
            currentRoom = 1
            scene = RemasterScenes.room(1)
            world.room = scene.room
            world.player.x = world.room.width - 38f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        } else if (currentRoom == 1 && world.player.x <= 2f) {
            currentRoom = 0
            scene = RemasterScenes.room(0)
            world.room = scene.room
            world.player.x = world.room.width - 38f
            world.player.y = 150f
            world.player.vx = 0f
            world.player.vy = 0f
        }
    }

    private fun sx(x:Float)= (x-viewport.x)*(width.toFloat()/viewport.width)
    private fun sy(y:Float)= (y-viewport.y)*(height.toFloat()/viewport.height)

    override fun onDraw(c: Canvas) {
        super.onDraw(c); if(width<=0||height<=0)return
        viewport.follow(world.player.x,world.player.y,world.room,width,height)
        val kx=width.toFloat()/viewport.width; val ky=height.toFloat()/viewport.height

        val sky=LinearGradient(0f,0f,0f,height.toFloat(),Color.rgb(35,83,132),Color.rgb(155,207,220),Shader.TileMode.CLAMP)
        p.shader=sky;c.drawRect(0f,0f,width.toFloat(),height.toFloat(),p);p.shader=null
        p.color=Color.argb(150,245,238,190);c.drawCircle(width*.78f,height*.17f,height*.08f,p)

        // distant forest: vector HD scenery, independent of NES pixels
        p.color=Color.rgb(41,91,70)
        for(i in -1..12){ val x=(i*92f-(viewport.x*.18f%92f))*kx; c.drawCircle(x,145f*ky,72f*kx,p) }
        p.color=Color.rgb(79,54,35)
        for(i in 0..8){ val x=(i*150f-(viewport.x*.45f%150f))*kx; c.drawRect(x,132f*ky,x+20f*kx,220f*ky,p) }

        for(pl in scene.platforms){
            val b=pl.box; p.color=when(pl.material){Material.GRASS->Color.rgb(67,118,55);Material.WOOD->Color.rgb(112,72,40);Material.STONE->Color.rgb(93,103,104)}
            c.drawRoundRect(sx(b.x),sy(b.y),sx(b.x+b.w),sy(b.y+b.h),8f,8f,p)
            if(pl.material==Material.GRASS){p.color=Color.rgb(117,164,69);c.drawRect(sx(b.x),sy(b.y),sx(b.x+b.w),sy(b.y+5f),p)}
        }

        drawDizzy(c,sx(world.player.x+8f),sy(world.player.y+12f),kx,ky)

        p.color=Color.argb(185,8,15,25);c.drawRoundRect(14f,14f,265f,62f,16f,16f,p)
        p.color=Color.WHITE;p.textSize=18f*resources.displayMetrics.scaledDensity;c.drawText(scene.title,28f,45f,p)
        drawControls(c)
    }

    private fun drawDizzy(c:Canvas,cx:Float,cy:Float,kx:Float,ky:Float){
        val bob=sin(t*8f)*1.2f*ky
        p.color=Color.rgb(247,244,226);c.drawOval(cx-11f*kx,cy-15f*ky+bob,cx+11f*kx,cy+13f*ky+bob,p)
        p.color=Color.rgb(38,45,53);c.drawCircle(cx-4f*kx,cy-4f*ky+bob,1.5f*kx,p);c.drawCircle(cx+4f*kx,cy-4f*ky+bob,1.5f*kx,p)
        p.style=Paint.Style.STROKE;p.strokeWidth=1.4f*kx;c.drawArc(cx-5f*kx,cy-1f*ky+bob,cx+5f*kx,cy+6f*ky+bob,10f,160f,false,p);p.style=Paint.Style.FILL
        p.color=Color.rgb(196,42,38);c.drawOval(cx-14f*kx,cy+9f*ky+bob,cx-1f*kx,cy+15f*ky+bob,p);c.drawOval(cx+1f*kx,cy+9f*ky+bob,cx+14f*kx,cy+15f*ky+bob,p)
    }

    private fun drawControls(c:Canvas){
        p.color=Color.argb(95,0,0,0); val y=height-86f
        c.drawCircle(70f,y,54f,p);c.drawCircle(width-70f,y,54f,p)
        p.color=Color.argb(210,255,255,255);p.textSize=32f;c.drawText("◀",34f,y+11f,p);c.drawText("▶",78f,y+11f,p);c.drawText("A",width-82f,y+11f,p)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        val down=e.actionMasked!=MotionEvent.ACTION_UP&&e.actionMasked!=MotionEvent.ACTION_CANCEL
        if(!down){input.left=false;input.right=false;input.a=false;lastA=false;return true}
        input.left=e.x<width*.18f; input.right=e.x>=width*.18f&&e.x<width*.36f; input.a=e.x>width*.72f\n        if(input.a && !lastA) audio.jump()\n        lastA=input.a
        return true
    }
}
