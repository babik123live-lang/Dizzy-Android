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
        3 -> scene(3,1152f,RoomCatalog.rooms[3].namePl,listOf(
            ScenePlatform(RectF(0f,210f,1152f,30f),Material.GRASS),
            ScenePlatform(RectF(90f,170f,180f,12f),Material.WOOD),
            ScenePlatform(RectF(330f,138f,210f,12f),Material.WOOD),
            ScenePlatform(RectF(610f,165f,175f,12f),Material.WOOD),
            ScenePlatform(RectF(850f,126f,205f,12f),Material.WOOD)
        ))
        4 -> scene(4,1024f,RoomCatalog.rooms[4].namePl,listOf(
            ScenePlatform(RectF(0f,210f,1024f,30f),Material.GRASS),
            ScenePlatform(RectF(110f,156f,170f,12f),Material.WOOD),
            ScenePlatform(RectF(350f,184f,155f,12f),Material.WOOD),
            ScenePlatform(RectF(565f,140f,180f,12f),Material.WOOD),
            ScenePlatform(RectF(810f,170f,165f,12f),Material.STONE)
        ))
        5 -> scene(5,1088f,RoomCatalog.rooms[5].namePl,listOf(
            ScenePlatform(RectF(0f,210f,1088f,30f),Material.GRASS),
            ScenePlatform(RectF(85f,180f,190f,12f),Material.STONE),
            ScenePlatform(RectF(330f,150f,175f,12f),Material.WOOD),
            ScenePlatform(RectF(565f,118f,205f,12f),Material.WOOD),
            ScenePlatform(RectF(835f,166f,190f,12f),Material.STONE)
        ))
        6 -> scene(6,1024f,RoomCatalog.rooms[6].namePl,listOf(
            ScenePlatform(RectF(0f,210f,1024f,30f),Material.GRASS),
            ScenePlatform(RectF(120f,174f,160f,12f),Material.WOOD),
            ScenePlatform(RectF(350f,142f,180f,12f),Material.WOOD),
            ScenePlatform(RectF(600f,176f,165f,12f),Material.WOOD),
            ScenePlatform(RectF(825f,132f,150f,12f),Material.STONE)
        ))
        7 -> scene(7,1088f,RoomCatalog.rooms[7].namePl,listOf(
            ScenePlatform(RectF(0f,210f,1088f,30f),Material.GRASS),
            ScenePlatform(RectF(80f,160f,175f,12f),Material.WOOD),
            ScenePlatform(RectF(315f,126f,190f,12f),Material.WOOD),
            ScenePlatform(RectF(570f,158f,190f,12f),Material.WOOD),
            ScenePlatform(RectF(825f,118f,200f,12f),Material.STONE)
        ))
        8 -> scene(8,1280f,RoomCatalog.rooms[8].namePl,listOf(
            ScenePlatform(RectF(0f,210f,1280f,30f),Material.GRASS),
            ScenePlatform(RectF(105f,170f,180f,12f),Material.WOOD),
            ScenePlatform(RectF(360f,135f,170f,12f),Material.WOOD),
            ScenePlatform(RectF(600f,102f,185f,12f),Material.WOOD),
            ScenePlatform(RectF(850f,142f,175f,12f),Material.WOOD),
            ScenePlatform(RectF(1080f,176f,155f,12f),Material.STONE)
        ))
        9 -> scene(9,1216f,RoomCatalog.rooms[9].namePl,listOf(
            ScenePlatform(RectF(0f,210f,1216f,30f),Material.GRASS),
            ScenePlatform(RectF(70f,165f,165f,12f),Material.WOOD),
            ScenePlatform(RectF(295f,125f,175f,12f),Material.WOOD),
            ScenePlatform(RectF(525f,88f,170f,12f),Material.WOOD),
            ScenePlatform(RectF(755f,122f,175f,12f),Material.WOOD),
            ScenePlatform(RectF(985f,162f,170f,12f),Material.WOOD)
        ))
        10 -> scene(10,1152f,RoomCatalog.rooms[10].namePl,listOf(
            ScenePlatform(RectF(0f,210f,1152f,30f),Material.GRASS),
            ScenePlatform(RectF(95f,176f,170f,12f),Material.WOOD),
            ScenePlatform(RectF(330f,142f,185f,12f),Material.WOOD),
            ScenePlatform(RectF(585f,172f,180f,12f),Material.WOOD),
            ScenePlatform(RectF(835f,134f,190f,12f),Material.STONE)
        ))
        else -> error("Room $id not remastered yet")
    }

    fun opening()=room(0)
}
