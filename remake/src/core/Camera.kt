package com.dizzy.remake.core

data class Camera(val worldHeight: Float = 240f) {
    fun worldWidth(viewWidthPx: Int, viewHeightPx: Int): Float {
        if (viewWidthPx <= 0 || viewHeightPx <= 0) return 256f
        return worldHeight * viewWidthPx.toFloat() / viewHeightPx.toFloat()
    }
}
