package com.dizzy.remake.core

sealed interface LivesSetting {
    data class Finite(val count:Int):LivesSetting { init { require(count in 1..99) } }
    data object Infinite:LivesSetting
}
class Lives(initial:LivesSetting=LivesSetting.Finite(3)) {
    var setting=initial; private set
    fun set(value:Int){ setting=LivesSetting.Finite(value.coerceIn(1,99)) }
    fun infinite(){ setting=LivesSetting.Infinite }
    fun lose():Boolean = when(val s=setting){
        LivesSetting.Infinite -> true
        is LivesSetting.Finite -> {
            val left=s.count-1
            if(left>0) setting=LivesSetting.Finite(left)
            left>0
        }
    }
    fun display()=when(val s=setting){ LivesSetting.Infinite -> "∞"; is LivesSetting.Finite -> s.count.toString() }
}
