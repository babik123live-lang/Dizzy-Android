package com.dizzy.remake.core

data class OriginalObjectIdentity(
    val sourceIndex:Int,
    val inventoryRecordCpu:Int,
    val inventoryGraphicsCpu:Int,
    val descriptionEn:String
)

/**
 * ROM-derived inventory/display identity for every persistent-object source
 * record. Original record bytes 7/8 point into bank 9. Routine $AC6A reads
 * that pointer, displays text beginning at +2, and returns the first word as
 * the bank-3 inventory graphic source used by the fixed-bank display path.
 */
object OriginalObjectIdentityCatalog {
    val all=listOf(
        OriginalObjectIdentity(0,0xAD45,0x8001,"NOTHING"),
        OriginalObjectIdentity(1,0xAD45,0x8001,"NOTHING"),
        OriginalObjectIdentity(2,0xB19C,0xAA3D,"A MACHINE WRENCH"),
        OriginalObjectIdentity(3,0xB1D1,0x8A04,"A FULL BOTTLE OF\nMEDICINE"),
        OriginalObjectIdentity(4,0xB223,0x894E,"AN EMPTY MEDICINE\nBOTTLE"),
        OriginalObjectIdentity(5,0xB273,0x8AB8,"A RED AND\nWHITE MUSHROOM"),
        OriginalObjectIdentity(6,0xB2C1,0x8B9D,"A MAGICAL STAR\nPLANT"),
        OriginalObjectIdentity(7,0xAD5B,0x8EE1,"A LONG LENGTH OF\nTOUGH ROPE"),
        OriginalObjectIdentity(8,0xB095,0x8257,"A ONE TON WEIGHT"),
        OriginalObjectIdentity(9,0xAE4A,0x8649,"DIZZY'S DOOR KEY"),
        OriginalObjectIdentity(10,0xB30D,0x8583,"THE KEY FOR\nDYLAN'S ELEVATOR"),
        OriginalObjectIdentity(11,0xB369,0x8583,"THE KEY FOR\nDENZIL'S ELEVATOR"),
        OriginalObjectIdentity(12,0xB3CA,0x8583,"THE KEY FOR GRAND\nDIZZY'S ELEVATOR"),
        OriginalObjectIdentity(13,0xAF16,0x8649,"DOZY'S DOOR KEY"),
        OriginalObjectIdentity(14,0xAE0A,0x8649,"DORA'S DOOR KEY"),
        OriginalObjectIdentity(15,0xAF56,0x8649,"DYLAN'S DOOR KEY"),
        OriginalObjectIdentity(16,0xAF99,0x8649,"GRAND DIZZY'S\nDOOR KEY"),
        OriginalObjectIdentity(17,0xAE8D,0x8649,"DENZIL'S DOOR KEY"),
        OriginalObjectIdentity(18,0xAED3,0x8649,"DAISY'S DOOR KEY"),
        OriginalObjectIdentity(19,0xADA9,0x8059,"A BRIDGEBUILDER'S\nSHARP AXE"),
        OriginalObjectIdentity(20,0xB0D0,0x8368,"AN AQUA LUNG FOR\nUNDER WATER"),
        OriginalObjectIdentity(21,0xAFED,0x8DB5,"A PAIR OF FLIPPERS\nFOR SWIMMING"),
        OriginalObjectIdentity(22,0xAD45,0x8001,"NOTHING"),
        OriginalObjectIdentity(23,0xAD45,0x8001,"NOTHING"),
        OriginalObjectIdentity(24,0xAD45,0x8001,"NOTHING"),
        OriginalObjectIdentity(25,0xAD45,0x8001,"NOTHING"),
        OriginalObjectIdentity(26,0xAD45,0x8001,"NOTHING"),
        OriginalObjectIdentity(27,0xAD45,0x8001,"NOTHING"),
        OriginalObjectIdentity(28,0xBB6B,0xA833,"A HEAVY BAG OF\nGOLD COINS"),
        OriginalObjectIdentity(29,0xB807,0x9427,"A MAGIC GREEN\nBEAN"),
        OriginalObjectIdentity(30,0xB540,0x90FD,"A LARGE BAG\nOF SALT"),
        OriginalObjectIdentity(31,0xB5EE,0xA593,"AN EMPTY OLD\nTIN BUCKET"),
        OriginalObjectIdentity(32,0xB70B,0xA4C0,"A BUCKET FULL OF\nWATER"),
        OriginalObjectIdentity(33,0xACAA,0x8FD6,"A TASTY LOOKING\nCOOKED CHICKEN"),
        OriginalObjectIdentity(34,0xB7A8,0x932E,"A KEY WITH A\nSKULL MOTIF"),
        OriginalObjectIdentity(35,0xB4F7,0x8842,"AN EMPTY TREASURE\nCHEST"),
        OriginalObjectIdentity(36,0xB587,0x9214,"DORA WHO HAS BEEN\nTURNED INTO A FROG"),
        OriginalObjectIdentity(37,0xB135,0x8496,"A CROSSBOW WITH\nLOTS OF BOLTS"),
        OriginalObjectIdentity(38,0xBA60,0x9E7C,"A VERY STRONG\nCROWBAR"),
        OriginalObjectIdentity(39,0xB91C,0x97A7,"A THICK PERSIAN\nRUG"),
        OriginalObjectIdentity(40,0xBA12,0x9BAB,"A PORTCULLIS\nWINCH WHEEL"),
        OriginalObjectIdentity(41,0xBCCE,0x9F3D,"A COMPLETE D.I.Y.\nROPE BRIDGE KIT"),
        OriginalObjectIdentity(42,0xBC77,0xA05F,"A GRAVEDIGGERS\nMUDDY SPADE"),
        OriginalObjectIdentity(43,0xBB0D,0x9CB7,"A PROTECTIVE\nOLD UMBRELLA"),
        OriginalObjectIdentity(44,0xBBC4,0x9DAF,"A GYMNASTS BOUNCY\nTRAMPET"),
        OriginalObjectIdentity(45,0xBC13,0xA124,"A HEAVY DUTY\nRUSTPROOF PICKAXE"),
        OriginalObjectIdentity(46,0xBAA7,0x9AC9,"AN INCREDIBLY\nSMALL PIGMY COW"),
        OriginalObjectIdentity(47,0xAD05,0x8C86,"A LARGE GOLD COIN"),
        OriginalObjectIdentity(48,0xB8C6,0x969C,"A WARM GOLDEN\nDRAGON EGG"),
        OriginalObjectIdentity(49,0xB8C6,0x969C,"A WARM GOLDEN\nDRAGON EGG"),
        OriginalObjectIdentity(50,0xB963,0x99BC,"A SMALL ANIMAL\nCAGE"),
        OriginalObjectIdentity(51,0xB9B0,0x98A0,"A CAGE CONTAINING\nPOGIE THE FLUFFLE"),
        OriginalObjectIdentity(52,0xB9B0,0x98A0,"A CAGE CONTAINING\nPOGIE THE FLUFFLE"),
        OriginalObjectIdentity(53,0xB84A,0x9500,"A DRY MATCH"),
        OriginalObjectIdentity(54,0xB87C,0x95C0,"A PILE OF\nDRY STRAW"),
        OriginalObjectIdentity(55,0xB4A2,0x8713,"AN OLD MEDICINE\nRECIPE"),
        OriginalObjectIdentity(56,0xB647,0xA6A1,"A BOTTLE OF SNAPPY\nWEED KILLER"),
        OriginalObjectIdentity(57,0xB752,0xA1F8,"A SOLID GOLD\nIRISH SHAMROCK"),
        OriginalObjectIdentity(58,0xBD37,0xA92B,"A PAIR OF BRASS\nCYMBALS"),
        OriginalObjectIdentity(59,0xB438,0x8583,"THE KEY FOR THE\nGROUND ELEVATOR"),
        OriginalObjectIdentity(60,0xB6AB,0xA772,"A LARGE STRONG\nPLANK OF WOOD"),
        OriginalObjectIdentity(61,0xB046,0x8157,"SOME STICKS OF\nDYNAMITE"),
        OriginalObjectIdentity(62,0xBD79,0xA2EE,"ZAKS' PORTCULLIS\nWINCH HANDLE"),
        OriginalObjectIdentity(63,0xBDDD,0xA3A7,"A BARREL OF\nPIRATES RUM")
    )

    init {
        require(all.size==OriginalObjectCatalog.RECORD_COUNT)
        require(all.map{it.sourceIndex}==(0 until OriginalObjectCatalog.RECORD_COUNT).toList())
        require(all.indices.all { i -> all[i].inventoryRecordCpu==OriginalObjectCatalog.all[i].raw78le })
        require(all.all { it.inventoryRecordCpu in 0x8000..0xBFFF })
        require(all.all { it.inventoryGraphicsCpu in 0x8000..0xBFFF })
    }

    operator fun get(sourceIndex:Int):OriginalObjectIdentity {
        require(sourceIndex in all.indices)
        return all[sourceIndex]
    }
}
