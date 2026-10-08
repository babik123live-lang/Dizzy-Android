package com.dizzy.remake.core

data class OriginalPersistentObjectDefinition(
    val sourceIndex:Int,
    val record:OriginalObject,
    val identity:OriginalObjectIdentity,
    val behavior:OriginalObjectBehaviorDescriptor
)

/**
 * Single native-facing view of the original persistent-object system.
 * This joins the 10-byte ROM record, its bank-9 inventory identity and the
 * exact behavior-dispatch target without inventing gameplay semantics.
 */
object OriginalPersistentObjectDefinitions {
    val all:List<OriginalPersistentObjectDefinition> =
        OriginalObjectCatalog.all.mapIndexed { index,record ->
            OriginalPersistentObjectDefinition(
                index,
                record,
                OriginalObjectIdentityCatalog[index],
                OriginalObjectBehaviorCatalog[record.behaviorIndex]
            )
        }

    init {
        require(all.size==64)
        require(all.map{it.sourceIndex}==(0 until 64).toList())
        require(all.all{it.identity.sourceIndex==it.sourceIndex})
        require(all.all{it.behavior.index==it.record.behaviorIndex})
    }

    fun forLocationKey(locationKey:Int):List<OriginalPersistentObjectDefinition> =
        all.filter { it.record.locationKey==locationKey }

    operator fun get(sourceIndex:Int):OriginalPersistentObjectDefinition {
        require(sourceIndex in all.indices)
        return all[sourceIndex]
    }
}
