package com.dizzy.remake.core

class FixedStepClock(private val hz: Int = 50) {
    private val stepNs = 1_000_000_000L / hz
    private var accumulator = 0L
    private var lastNs = 0L

    fun reset(nowNs: Long) { lastNs = nowNs; accumulator = 0L }

    fun advance(nowNs: Long, tick: () -> Unit) {
        if (lastNs == 0L) { reset(nowNs); return }
        val elapsed = (nowNs - lastNs).coerceIn(0L, 250_000_000L)
        lastNs = nowNs
        accumulator += elapsed
        while (accumulator >= stepNs) {
            tick()
            accumulator -= stepNs
        }
    }
}
