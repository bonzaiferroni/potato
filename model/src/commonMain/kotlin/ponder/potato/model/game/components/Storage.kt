package ponder.potato.model.game.components

import ponder.potato.model.game.EntityMap
import ponder.potato.model.game.Resource
import ponder.potato.model.game.entities.EntityState
import ponder.potato.model.game.entities.StateEntity
import ponder.potato.model.game.sumOf

interface StorageState: EntityState {
    val storage: Double
    fun isStorageType(resource: Resource): Boolean
}

class Storage(
    override val entity: StateEntity<StorageState>
): StateComponent<StorageState>() {
}

fun EntityMap.readResourceMax(resource: Resource) = this.sumOf<StorageState> {
    if (it.isStorageType(resource)) it.storage else 0.0
}