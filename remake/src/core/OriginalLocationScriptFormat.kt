package com.dizzy.remake.core

/**
 * Structural facts about the original location-script dispatcher at $F439.
 * Scripts live in bank 1 and contain 16-bit handler addresses. Some handlers
 * consume immediate argument bytes from the same stream before returning to
 * the dispatcher.
 */
object OriginalLocationScriptFormat {
    const val DISPATCHER_CPU = 0xF439
    const val SCRIPT_BANK = 1
    const val TERMINATOR_HIGH_BYTE = 0

    private val argumentCounts = mapOf(
        0xF498 to 3,
        0xF46F to 1,
        0xF492 to 1,
        0xF4C1 to 5
    )

    fun immediateArgumentCount(handlerCpu:Int):Int = argumentCounts[handlerCpu] ?: 0
    fun isKnownArgumentConsumer(handlerCpu:Int):Boolean = handlerCpu in argumentCounts
}
