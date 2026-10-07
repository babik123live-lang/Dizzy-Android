package com.dizzy.remake.core

data class GameState(
    var livesCount: Int = 3,
    var infiniteLives: Boolean = false,
    var health: Int = 0,
    var oxygen: Int = 0,
    var tick: Long = 0
) {
    fun onDeath(): Boolean {
        if (!infiniteLives) livesCount = (livesCount - 1).coerceAtLeast(0)
        return infiniteLives || livesCount > 0
    }
}
