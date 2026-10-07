package com.dizzy.remake.tests
import com.dizzy.remake.core.*

fun main(){
    check(RoomCatalog.rooms.size==49)
    check(RoomCatalog.rooms.first().namePl=="Dom Dizzy'ego")
    check(RoomCatalog.rooms.last().region==Region.OCEAN_CAVE)
    val b=WideWorldBuilder()
    val strip=b.build(Region.TREEHOUSE,listOf(0,1,2),mapOf(0 to 256f,1 to 512f,2 to 256f))
    check(strip.width==1024f)
    check(strip.localToWorld(1,20f)==276f)
    val lives=Lives();repeat(2){check(lives.lose())};check(!lives.lose())
    lives.infinite();repeat(100){check(lives.lose())};check(lives.display()=="∞")
    println("world catalog self-test OK")
}
