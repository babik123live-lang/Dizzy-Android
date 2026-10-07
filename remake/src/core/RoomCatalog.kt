package com.dizzy.remake.core

enum class Region { TREEHOUSE, COUNTRYSIDE, COAST, GRAVEYARD, PIRATE_SHIP, KELDOR, CLOUDS, ZAK_CASTLE, TUNNELS, MINES, TROLL_CASTLE, OCEAN_CAVE }

data class RoomDescriptor(val textId:Int,val region:Region,val namePl:String)

/**
 * Provisional Polish display labels keyed 0..49.
 * They never drive original geometry, transitions or identity-sensitive logic;
 * those come from the ROM-derived catalogs. Exact naming remains separate from
 * the runtime location reconstruction because some original text pointers are
 * conditionally reused or adjusted by game code.
 */
object RoomCatalog {
    val rooms=listOf(
        RoomDescriptor(0,Region.TREEHOUSE,"Dom Dizzy'ego"),
        RoomDescriptor(1,Region.TREEHOUSE,"Dom Dylana"),
        RoomDescriptor(2,Region.TREEHOUSE,"Dom Daisy"),
        RoomDescriptor(3,Region.TREEHOUSE,"Sala spotkań"),
        RoomDescriptor(4,Region.TREEHOUSE,"Dom Dory"),
        RoomDescriptor(5,Region.TREEHOUSE,"Dom Grand Dizzy'ego"),
        RoomDescriptor(6,Region.TREEHOUSE,"Dom Dozy'ego"),
        RoomDescriptor(7,Region.TREEHOUSE,"Dom Denzila"),
        RoomDescriptor(8,Region.TREEHOUSE,"Górna część wioski"),
        RoomDescriptor(9,Region.TREEHOUSE,"Wysoko w koronach drzew"),
        RoomDescriptor(10,Region.TREEHOUSE,"Okolice domu Dylana"),
        RoomDescriptor(11,Region.TREEHOUSE,"Centrum wioski"),
        RoomDescriptor(12,Region.TREEHOUSE,"Ścieżki przy Denzilu"),
        RoomDescriptor(13,Region.TREEHOUSE,"Okolice domu Daisy"),
        RoomDescriptor(14,Region.TREEHOUSE,"Ścieżki przy domu Dizzy'ego"),
        RoomDescriptor(15,Region.TREEHOUSE,"Podstawa wioski Yolkfolk"),
        RoomDescriptor(16,Region.MINES,"Kopalnia diamentów – windy"),
        RoomDescriptor(17,Region.COUNTRYSIDE,"Stary drewniany most"),
        RoomDescriptor(18,Region.COAST,"Carber Bay"),
        RoomDescriptor(19,Region.COAST,"Crystal Falls"),
        RoomDescriptor(20,Region.GRAVEYARD,"Jaskinia pod cmentarzem"),
        RoomDescriptor(21,Region.COAST,"Wody Carber Bay"),
        RoomDescriptor(22,Region.COAST,"Wrak pirackiego galeonu"),
        RoomDescriptor(23,Region.GRAVEYARD,"Nawiedzony cmentarz"),
        RoomDescriptor(24,Region.COUNTRYSIDE,"Łąki i zerwany most"),
        RoomDescriptor(25,Region.PIRATE_SHIP,"Takielunek statku Blackhearta"),
        RoomDescriptor(26,Region.PIRATE_SHIP,"Górny pokład statku"),
        RoomDescriptor(27,Region.PIRATE_SHIP,"Wnętrze statku Blackhearta I"),
        RoomDescriptor(28,Region.PIRATE_SHIP,"Wnętrze statku Blackhearta II"),
        RoomDescriptor(29,Region.KELDOR,"Castle Street"),
        RoomDescriptor(30,Region.KELDOR,"Bridge Street"),
        RoomDescriptor(31,Region.KELDOR,"Dock Street"),
        RoomDescriptor(32,Region.CLOUDS,"Chmury"),
        RoomDescriptor(33,Region.CLOUDS,"Okolice zamku Zaksa"),
        RoomDescriptor(34,Region.ZAK_CASTLE,"Podejście do wieży Zaksa"),
        RoomDescriptor(35,Region.ZAK_CASTLE,"Centralna część zamku Zaksa"),
        RoomDescriptor(36,Region.ZAK_CASTLE,"Dolny poziom zamku Zaksa"),
        RoomDescriptor(37,Region.TUNNELS,"Tunel Dock–Castle"),
        RoomDescriptor(38,Region.TUNNELS,"Tunel Castle–Bridge"),
        RoomDescriptor(39,Region.TUNNELS,"Przejście Castle–Bridge"),
        RoomDescriptor(40,Region.TUNNELS,"Długi tunel Dock–Castle"),
        RoomDescriptor(41,Region.TUNNELS,"Przejście Dock–Bridge"),
        RoomDescriptor(42,Region.TUNNELS,"Przejście Castle–Dock"),
        RoomDescriptor(43,Region.TUNNELS,"Upiorny tunel Castle–Bridge"),
        RoomDescriptor(44,Region.TROLL_CASTLE,"Hol zamku trolli"),
        RoomDescriptor(45,Region.MINES,"Kopalnia diamentów – windy"),
        RoomDescriptor(46,Region.MINES,"Kopalnia – stary tor"),
        RoomDescriptor(47,Region.MINES,"Najgłębsze tunele kopalni"),
        RoomDescriptor(48,Region.ZAK_CASTLE,"Głębia zamku"),
        RoomDescriptor(49,Region.OCEAN_CAVE,"Jaskinia pod dnem oceanu")
    )
    init {
        require(rooms.size == OriginalLocationCatalog.COUNT)
        require(rooms.map{it.textId} == (0 until OriginalLocationCatalog.COUNT).toList())
    }
}
