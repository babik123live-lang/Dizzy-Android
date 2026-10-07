package com.dizzy.remake.core

data class ScenePlatform(val box: RectF, val material: Material)
enum class Material { GRASS, WOOD, STONE }

data class RemasterScene(
    val room: Room,
    val platforms: List<ScenePlatform>,
    val title: String
)

object RemasterScenes {
    fun opening(): RemasterScene {
        val platforms = listOf(
            ScenePlatform(RectF(0f, 210f, 2048f, 30f), Material.GRASS),
            ScenePlatform(RectF(235f, 172f, 180f, 12f), Material.WOOD),
            ScenePlatform(RectF(520f, 146f, 150f, 12f), Material.WOOD),
            ScenePlatform(RectF(760f, 188f, 230f, 12f), Material.STONE)
        )
        return RemasterScene(
            Room(0, 2048f, 240f, platforms.map { Solid(it.box) }),
            platforms,
            RoomCatalog.rooms.first().namePl
        )
    }
}
