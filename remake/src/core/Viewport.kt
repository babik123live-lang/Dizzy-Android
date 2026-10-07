package com.dizzy.remake.core
data class Viewport(var x:Float=0f,var y:Float=0f,var width:Float=256f,var height:Float=240f){
    fun follow(px:Float,py:Float,room:Room,screenW:Int,screenH:Int){
        height=240f
        width=(height*screenW.toFloat()/screenH.coerceAtLeast(1)).coerceAtLeast(256f)
        x=(px-width/2f).coerceIn(0f,(room.width-width).coerceAtLeast(0f))
        y=(py-height/2f).coerceIn(0f,(room.height-height).coerceAtLeast(0f))
    }
}
