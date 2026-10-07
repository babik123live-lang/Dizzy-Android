package com.dizzy.remake.core

/**
 * ROM-derived persistent-object records. These are data records, not guessed
 * scene geometry. areaId is the original area; subAreaId is retained exactly
 * as stored by the game and must not be treated as a RoomCatalog id without
 * further verification.
 */
object OriginalObjectCatalog {
    private val rows = arrayOf(
        intArrayOf(17,102,5,120,43525,31,45468,0),
        intArrayOf(5,78,2,240,35279,14,45521,0),
        intArrayOf(11,73,3,104,35096,2,45603,1),
        intArrayOf(17,108,2,126,35453,2,45683,2),
        intArrayOf(12,69,3,40,35684,2,45761,3),
        intArrayOf(14,40,5,104,36519,4,44379,0),
        intArrayOf(10,198,4,136,33305,5,45205,0),
        intArrayOf(0,100,0,40,34323,1,44618,0),
        intArrayOf(11,220,5,104,34114,3,45837,3),
        intArrayOf(14,44,1,136,34114,3,45929,1),
        intArrayOf(45,214,2,120,34114,3,46026,2),
        intArrayOf(2,110,0,158,34323,1,44822,1),
        intArrayOf(12,170,0,168,34323,1,44554,2),
        intArrayOf(10,32,2,104,34323,1,44886,3),
        intArrayOf(3,60,0,118,34323,1,44953,4),
        intArrayOf(15,164,7,72,34323,1,44685,5),
        intArrayOf(13,74,6,30,34323,1,44755,6),
        intArrayOf(46,8,1,144,32804,6,44457,0),
        intArrayOf(18,26,3,128,33581,0,45264,0),
        intArrayOf(7,184,0,240,36217,0,45037,0),
        intArrayOf(31,85,4,160,36995,0,44357,0),
        intArrayOf(19,220,0,128,36995,0,44357,0),
        intArrayOf(34,52,3,38,36995,0,44357,0),
        intArrayOf(24,236,7,104,36995,0,44357,0),
        intArrayOf(14,68,9,104,36995,0,44357,0),
        intArrayOf(24,156,0,158,36995,0,44357,0),
        intArrayOf(19,161,2,128,42996,26,47979,0),
        intArrayOf(29,166,3,240,37876,7,47111,0),
        intArrayOf(18,56,10,111,37059,8,46400,0),
        intArrayOf(20,244,1,80,42118,0,46574,0),
        intArrayOf(20,132,3,134,42118,30,46859,0),
        intArrayOf(15,4,1,160,36761,9,44202,0),
        intArrayOf(23,144,6,44,37618,3,47016,4),
        intArrayOf(22,140,5,72,34826,10,46327,0),
        intArrayOf(4,130,0,126,37331,11,46471,0),
        intArrayOf(15,211,1,240,33879,0,45365,0),
        intArrayOf(30,162,6,112,40528,12,47712,0),
        intArrayOf(6,173,0,240,38761,13,47388,0),
        intArrayOf(25,200,0,124,39786,15,47634,0),
        intArrayOf(48,190,0,64,40700,16,48334,0),
        intArrayOf(30,60,0,78,41009,17,48247,0),
        intArrayOf(29,36,9,143,40058,0,47885,0),
        intArrayOf(20,120,0,80,40307,0,48068,0),
        intArrayOf(18,56,1,80,41194,18,48147,0),
        intArrayOf(1,250,0,240,39565,19,47783,0),
        intArrayOf(5,148,0,240,38492,0,47302,0),
        intArrayOf(46,144,6,160,38492,0,47302,0),
        intArrayOf(2,170,0,134,39298,20,47459,0),
        intArrayOf(13,76,9,240,39006,22,47536,0),
        intArrayOf(14,76,9,240,39006,22,47536,0),
        intArrayOf(24,10,4,96,38089,21,47178,0),
        intArrayOf(12,202,3,168,38273,0,47228,0),
        intArrayOf(5,54,1,62,34512,0,46242,0),
        intArrayOf(15,194,6,158,42600,28,46663,0),
        intArrayOf(46,58,7,120,41405,23,46930,0),
        intArrayOf(45,8,7,90,43245,24,48439,0),
        intArrayOf(15,139,2,140,34114,3,46136,0),
        intArrayOf(14,244,1,168,42810,29,46763,0),
        intArrayOf(28,248,3,62,33049,25,45126,0),
        intArrayOf(27,12,3,96,41657,1,48505,7),
        intArrayOf(19,172,1,64,41833,27,48605,0)
    )

    val all: List<OriginalObject> = rows.map { r ->
        OriginalObject(r[0],r[1],r[2],r[3],r[4],r[5],r[6],r[7])
    }

    fun inArea(areaId:Int): List<OriginalObject> = all.filter { it.areaId == areaId }
}
