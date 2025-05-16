package ponder.potato.model.game

interface EntityStorageState: EntityState {
    val capacity: Double
    val resource: Resource
    fun isStorageType(resource: Resource) = this.resource == resource
}

class EntityStorage(
    override val entity: StateEntity<EntityStorageState>
): StateComponent<EntityStorageState>() {
}

fun EntityMap.readResourceMax(resource: Resource) = this.sumOf<EntityStorageState> {
    if (it.isStorageType(resource)) it.capacity else 0.0
}