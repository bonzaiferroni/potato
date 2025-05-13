package ponder.potato.model.game.components

import ponder.potato.model.game.EntityMap
import ponder.potato.model.game.Resource
import ponder.potato.model.game.entities.EntityState
import ponder.potato.model.game.entities.StateEntity
import ponder.potato.model.game.sumOf

interface EntityStorageState: EntityState {
    val storedValue: Double
    val storedResource: Resource
    fun isStorageType(resource: Resource) = this.storedResource == resource
}

class EntityStorage(
    override val entity: StateEntity<EntityStorageState>
): StateComponent<EntityStorageState>() {
}

fun EntityMap.readResourceMax(resource: Resource) = this.sumOf<EntityStorageState> {
    if (it.isStorageType(resource)) it.storedValue else 0.0
}