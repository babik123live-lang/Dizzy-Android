package com.dizzy.remake.core

data class ScenePlatform(val box: RectF, val material: Material)
enum class Material { GRASS, WOOD, STONE }

data class RemasterScene(
    val room: Room,
    val platforms: List<ScenePlatform>,
    val title: String
)

object RemasterScenes {
    private fun scene(id:Int, width:Float, title:String, platforms:List<ScenePlatform>) =
        RemasterScene(Room(id,width,240f,platforms.map{Solid(it.box)}),platforms,title)

    fun room(id:Int):RemasterScene = when(id) {
        0 -> scene(0,1024f,RoomCatalog.rooms[0].namePl,listOf(
            ScenePlatform(RectF(0f,210f,1024f,30f),Material.GRASS),
            ScenePlatform(RectF(235f,172f,180f,12f),Material.WOOD),
            ScenePlatform(RectF(520f,146f,150f,12f),Material.WOOD),
            ScenePlatform(RectF(760f,188f,230f,12f),Material.STONE)
        ))
        1 -> scene(1,1024f,RoomCatalog.rooms[1].namePl,listOf(
            ScenePlatform(RectF(0f,210f,1024f,30f),Material.GRASS),
            ScenePlatform(RectF(100f,164f,180f,12f),Material.WOOD),
            ScenePlatform(RectF(390f,135f,210f,12f),Material.WOOD),
            ScenePlatform(RectF(720f,174f,180f,12f),Material.WOOD)
        ))
        2 -> scene(2,1024f,RoomCatalog.rooms[2].namePl,listOf(
            ScenePlatform(RectF(0f,210f,1024f,30f),Material.GRASS),
            ScenePlatform(RectF(145f,178f,150f,12f),Material.WOOD),
            ScenePlatform(RectF(350f,145f,190f,12f),Material.WOOD),
            ScenePlatform(RectF(615f,120f,150f,12f),Material.WOOD),
            ScenePlatform(RectF(825f,174f,160f,12f),Material.STONE)
        ))
        else -> error("Room $id not remastered yet")
    }

    fun opening()=room(0)
}
