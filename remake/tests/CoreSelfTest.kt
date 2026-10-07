package com.dizzy.remake.tests
import com.dizzy.remake.core.*

fun main(){
    val floor=Solid(RectF(0f,100f,1000f,20f))
    val w=World(Room(0,1000f,240f,listOf(floor)),Player(50f,0f))
    repeat(100){w.tick(false,false,false)}
    check(w.player.grounded)
    check(w.player.y==76f)
    val s=GameState(lives=2)
    check(s.onDeath() && s.lives==1)
    check(!s.onDeath() && s.lives==0)
    s.lives=1;s.infiniteLives=true;repeat(20){check(s.onDeath())};check(s.lives==1)
    val v=Viewport();v.follow(500f,120f,w.room,1950,900)
    check(v.width>500f)
    println("core self-test OK")
}
