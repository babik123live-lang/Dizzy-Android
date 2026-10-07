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
        11 -> scene(11,1280f,RoomCatalog.rooms[11].namePl,listOf(
            ScenePlatform(RectF(0f,210f,1280f,30f),Material.GRASS),
            ScenePlatform(RectF(85f,172f,180f,12f),Material.WOOD),
            ScenePlatform(RectF(335f,138f,180f,12f),Material.WOOD),
            ScenePlatform(RectF(585f,104f,190f,12f),Material.WOOD),
            ScenePlatform(RectF(845f,142f,180f,12f),Material.WOOD),
            ScenePlatform(RectF(1080f,176f,150f,12f),Material.STONE)
        ))
        12 -> scene(12,1152f,RoomCatalog.rooms[12].namePl,listOf(
            ScenePlatform(RectF(0f,210f,1152f,30f),Material.GRASS),
            ScenePlatform(RectF(90f,176f,165f,12f),Material.WOOD),
            ScenePlatform(RectF(320f,146f,180f,12f),Material.WOOD),
            ScenePlatform(RectF(570f,112f,175f,12f),Material.WOOD),
            ScenePlatform(RectF(810f,150f,185f,12f),Material.WOOD)
        ))
        13 -> scene(13,1152f,RoomCatalog.rooms[13].namePl,listOf(
            ScenePlatform(RectF(0f,210f,1152f,30f),Material.GRASS),
            ScenePlatform(RectF(105f,166f,175f,12f),Material.WOOD),
            ScenePlatform(RectF(345f,132f,185f,12f),Material.WOOD),
            ScenePlatform(RectF(600f,164f,170f,12f),Material.WOOD),
            ScenePlatform(RectF(835f,126f,190f,12f),Material.STONE)
        ))
        14 -> scene(14,1216f,RoomCatalog.rooms[14].namePl,listOf(
            ScenePlatform(RectF(0f,210f,1216f,30f),Material.GRASS),
            ScenePlatform(RectF(85f,174f,170f,12f),Material.WOOD),
            ScenePlatform(RectF(320f,138f,180f,12f),Material.WOOD),
            ScenePlatform(RectF(565f,104f,185f,12f),Material.WOOD),
            ScenePlatform(RectF(815f,144f,175f,12f),Material.WOOD),
            ScenePlatform(RectF(1040f,178f,130f,12f),Material.STONE)
        ))
        15 -> scene(15,1280f,RoomCatalog.rooms[15].namePl,listOf(
            ScenePlatform(RectF(0f,210f,1280f,30f),Material.GRASS),
            ScenePlatform(RectF(95f,176f,175f,12f),Material.WOOD),
            ScenePlatform(RectF(340f,144f,185f,12f),Material.WOOD),
            ScenePlatform(RectF(595f,112f,190f,12f),Material.WOOD),
            ScenePlatform(RectF(855f,150f,180f,12f),Material.WOOD),
            ScenePlatform(RectF(1090f,180f,145f,12f),Material.STONE)
        ))
        16 -> scene(16,1344f,RoomCatalog.rooms[16].namePl,listOf(
            ScenePlatform(RectF(0f,210f,1344f,30f),Material.GRASS),
            ScenePlatform(RectF(110f,176f,190f,12f),Material.WOOD),
            ScenePlatform(RectF(365f,156f,220f,12f),Material.WOOD),
            ScenePlatform(RectF(650f,176f,220f,12f),Material.WOOD),
            ScenePlatform(RectF(940f,154f,205f,12f),Material.WOOD),
            ScenePlatform(RectF(1190f,184f,120f,12f),Material.STONE)
        ))
        17 -> scene(17,1408f,RoomCatalog.rooms[17].namePl,listOf(
            ScenePlatform(RectF(0f,210f,1408f,30f),Material.STONE),
            ScenePlatform(RectF(90f,178f,180f,12f),Material.WOOD),
            ScenePlatform(RectF(340f,150f,190f,12f),Material.WOOD),
            ScenePlatform(RectF(605f,182f,185f,12f),Material.STONE),
            ScenePlatform(RectF(865f,146f,195f,12f),Material.WOOD),
            ScenePlatform(RectF(1135f,176f,210f,12f),Material.STONE)
        ))
        18 -> scene(18,1344f,RoomCatalog.rooms[18].namePl,listOf(
            ScenePlatform(RectF(0f,210f,1344f,30f),Material.STONE),
            ScenePlatform(RectF(90f,174f,170f,12f),Material.STONE),
            ScenePlatform(RectF(330f,136f,185f,12f),Material.STONE),
            ScenePlatform(RectF(585f,96f,180f,12f),Material.STONE),
            ScenePlatform(RectF(835f,132f,190f,12f),Material.STONE),
            ScenePlatform(RectF(1095f,172f,185f,12f),Material.STONE)
        ))
        19 -> scene(19,1280f,RoomCatalog.rooms[19].namePl,listOf(
            ScenePlatform(RectF(0f,210f,1280f,30f),Material.STONE),
            ScenePlatform(RectF(100f,172f,180f,12f),Material.STONE),
            ScenePlatform(RectF(350f,136f,170f,12f),Material.STONE),
            ScenePlatform(RectF(590f,104f,185f,12f),Material.STONE),
            ScenePlatform(RectF(845f,142f,180f,12f),Material.STONE),
            ScenePlatform(RectF(1085f,178f,150f,12f),Material.STONE)
        ))
        else -> error("Room $id not remastered yet")
    }

    fun opening()=room(0)
}
