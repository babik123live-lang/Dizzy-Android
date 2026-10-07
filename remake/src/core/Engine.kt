package com.dizzy.remake.core

class Engine(
    var world:World,
    val input:InputState=InputState(),
    val state:GameState=GameState(),
    val health:Health=Health(),
    val oxygen:Oxygen=Oxygen()
){
    private var previousJump=false
    fun tick(){
        val jumpPressed=input.a && !previousJump
        previousJump=input.a
        world.tick(input.left,input.right,jumpPressed)
        health.tick()
        state.health=health.damage
        state.oxygen=oxygen.value
        state.tick++
    }
}
