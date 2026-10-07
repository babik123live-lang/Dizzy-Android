package com.dizzy.remake.core

class Health {
    // Original RAM $F2 is the true damage level; $F3 visually catches up.
    var damage=0; private set
    var displayDamage=0; private set
    val dead get() = damage >= 0x5f

    fun hit(amount:Int){ damage=(damage+amount).coerceAtMost(0x5f) }
    fun tick(){ if(displayDamage<damage) displayDamage++ }
    fun reset(){ damage=0; displayDamage=0 }
}
