package com.dizzy.remake
import android.content.Context
import android.graphics.*
import android.view.*
import com.dizzy.remake.core.*

class GameView(c:Context):SurfaceView(c),SurfaceHolder.Callback,Runnable{
 private var running=false; private var thread:Thread?=null
 private val clock=FixedStepClock()
 private val input=InputState()
 private val world=World(Room(0,2048f,240f,listOf(Solid(com.dizzy.remake.core.RectF(0f,220f,2048f,20f)))),Player(128f,180f))
 private val engine=Engine(world,input)
 private val viewport=Viewport()
 private val paint=Paint(Paint.ANTI_ALIAS_FLAG)
 init{holder.addCallback(this);isFocusable=true}
 override fun surfaceCreated(h:SurfaceHolder){running=true;thread=Thread(this,"DizzyGame").also{it.start()}}
 override fun surfaceDestroyed(h:SurfaceHolder){running=false;thread?.join(500)}
 override fun surfaceChanged(h:SurfaceHolder,f:Int,w:Int,hh:Int){}
 override fun run(){clock.reset(System.nanoTime());while(running){clock.advance(System.nanoTime()){engine.tick()};drawFrame()}}
 private fun drawFrame(){val c=holder.lockCanvas()?:return;try{
  val sx=width/viewport.width;val sy=height/viewport.height
  c.drawColor(Color.rgb(25,35,55));paint.color=Color.rgb(58,104,61)
  c.drawRect(0f,220f*sy,width.toFloat(),height.toFloat(),paint)
  viewport.follow(world.player.x,world.player.y,world.room,width,height)
  paint.color=Color.WHITE
  c.drawOval((world.player.x-viewport.x)*sx,(world.player.y-viewport.y)*sy,(world.player.x-viewport.x+24)*sx,(world.player.y-viewport.y+24)*sy,paint)
  paint.textSize=18f*resources.displayMetrics.density;c.drawText("Dizzy • 50 Hz • szeroki świat",20f,32f*resources.displayMetrics.density,paint)
 }finally{holder.unlockCanvasAndPost(c)}}
 override fun onTouchEvent(e:MotionEvent):Boolean{
  val down=e.actionMasked!=MotionEvent.ACTION_UP && e.actionMasked!=MotionEvent.ACTION_CANCEL
  if(!down){input.left=false;input.right=false;input.a=false;return true}
  input.left=e.x<width*.25f;input.right=e.x in width*.25f..width*.5f;input.a=e.x>width*.72f
  return true
 }
}
