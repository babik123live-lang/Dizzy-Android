package com.dizzy.remake.core

class Oxygen(private val maximum:Int=0xff) {
    var value=maximum; private set
    fun reset(){ value=maximum }
    fun consume(amount:Int=1){ value=(value-amount).coerceAtLeast(0) }
    val empty get()=value==0
}
