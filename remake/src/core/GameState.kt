package com.dizzy.remake.core

data class GameState(
    var lives: Int = 3,
    var infiniteLives: Boolean = false,
    var health: Int = 0,
    var oxygen: Int = 0,
    var tick: Long = 0
) {
    fun onDeath(): Boolean {
        if (!infiniteLives) lives = (lives - 1).coerceAtLeast(0)
        return infiniteLives || lives > 0
    }
}
