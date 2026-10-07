package com.dizzy.remake.core

data class OriginalScriptSpriteFrame(
    val topTile:Int,
    val bottomTile:Int,
    val attributes:Int
)

/** Verified rendering math used by original handler $F498 via $87EB. */
object OriginalScriptSpriteRenderContract {
    const val HANDLER_CPU=0xF498
    const val RENDER_CPU=0x87EB
    const val TOP_Y_OFFSET=-8
    const val BOTTOM_Y_OFFSET=0

    fun frame(animationState48:Int):OriginalScriptSpriteFrame {
        require(animationState48 in 0..255)
        val top=0x84 + ((animationState48 ushr 2) and 0x02)
        val attributes=0x01 or ((animationState48 shl 2) and 0x40)
        return OriginalScriptSpriteFrame(top,top+1,attributes)
    }
}
