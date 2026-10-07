package com.dizzy.remake.core

enum class Region { TREEHOUSE, COUNTRYSIDE, COAST, GRAVEYARD, PIRATE_SHIP, KELDOR, CLOUDS, ZAK_CASTLE, TUNNELS, MINES, TROLL_CASTLE, OCEAN_CAVE }

data class RoomDescriptor(val textId:Int,val region:Region,val namePl:String)

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
        RoomDescriptor(9,Region.TREEHOUSE,"Korony drzew"),
        RoomDescriptor(10,Region.TREEHOUSE,"Okolice domu Dylana"),
        RoomDescriptor(11,Region.TREEHOUSE,"Centrum wioski"),
        RoomDescriptor(12,Region.TREEHOUSE,"Ścieżki przy Denzilu"),
        RoomDescriptor(13,Region.TREEHOUSE,"Okolice domu Daisy"),
        RoomDescriptor(14,Region.TREEHOUSE,"Ścieżki przy domu Dizzy'ego"),
        RoomDescriptor(15,Region.TREEHOUSE,"Podstawa wioski Yolkfolk"),
        RoomDescriptor(16,Region.COUNTRYSIDE,"Stary drewniany most"),
        RoomDescriptor(17,Region.COAST,"Carber Bay"),
        RoomDescriptor(18,Region.COAST,"Crystal Falls"),
        RoomDescriptor(19,Region.GRAVEYARD,"Jaskinia pod cmentarzem"),
        RoomDescriptor(20,Region.COAST,"Wody Carber Bay"),
        RoomDescriptor(21,Region.COAST,"Wrak galeonu"),
        RoomDescriptor(22,Region.GRAVEYARD,"Cmentarz"),
        RoomDescriptor(23,Region.COUNTRYSIDE,"Łąki i zerwany most"),
        RoomDescriptor(24,Region.PIRATE_SHIP,"Takielunek statku"),
        RoomDescriptor(25,Region.PIRATE_SHIP,"Górny pokład"),
        RoomDescriptor(26,Region.PIRATE_SHIP,"Wnętrze statku"),
        RoomDescriptor(27,Region.PIRATE_SHIP,"Ładownia"),
        RoomDescriptor(28,Region.KELDOR,"Castle Street"),
        RoomDescriptor(29,Region.KELDOR,"Bridge Street"),
        RoomDescriptor(30,Region.KELDOR,"Dock Street"),
        RoomDescriptor(31,Region.CLOUDS,"Chmury"),
        RoomDescriptor(32,Region.CLOUDS,"Podejście do zamku Zaksa"),
        RoomDescriptor(33,Region.ZAK_CASTLE,"Wieża Zaksa"),
        RoomDescriptor(34,Region.ZAK_CASTLE,"Centralny zamek Zaksa"),
        RoomDescriptor(35,Region.ZAK_CASTLE,"Dolny poziom zamku"),
        RoomDescriptor(36,Region.TUNNELS,"Tunel Dock–Castle"),
        RoomDescriptor(37,Region.TUNNELS,"Tunel Castle–Bridge"),
        RoomDescriptor(38,Region.TUNNELS,"Przejście Castle–Bridge"),
        RoomDescriptor(39,Region.TUNNELS,"Długi tunel Dock–Castle"),
        RoomDescriptor(40,Region.TUNNELS,"Przejście Dock–Bridge"),
        RoomDescriptor(41,Region.TUNNELS,"Przejście Castle–Dock"),
        RoomDescriptor(42,Region.TUNNELS,"Upiorny tunel Castle–Bridge"),
        RoomDescriptor(43,Region.MINES,"Kopalnia diamentów – windy"),
        RoomDescriptor(44,Region.MINES,"Kopalnia – stary tor"),
        RoomDescriptor(45,Region.MINES,"Najgłębsze tunele kopalni"),
        RoomDescriptor(46,Region.ZAK_CASTLE,"Głębia zamku"),
        RoomDescriptor(47,Region.TROLL_CASTLE,"Hol zamku trolli"),
        RoomDescriptor(48,Region.OCEAN_CAVE,"Jaskinia pod dnem oceanu")
    )
    init { require(rooms.map{it.textId}==(0..48).toList()) }
}
