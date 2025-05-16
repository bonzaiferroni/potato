package ponder.potato.model.game

import kotlinx.serialization.Serializable

class GardenBed(
    override val state: GardenBedState = GardenBedState()
): StateEntity<GardenBedState>() {
    override val components: List<StateComponent<*>> get() = emptyList()

    fun fillDirt(): EntityAction? {
        if (state.isFullOfDirt || game.storage.readQuantity(Resource.Dirt) < state.capacity) return null
        return EntityAction(
            entityId = id,
            ability = EntityAbility.Fill,
        ) {
            val removed = game.storage.removeQuantity(Resource.Dirt, state.capacity)
            if (removed) {
                state.isFullOfDirt = true
            }
        }
    }

    override val abilities: List<() -> EntityAction?> get() = listOf(
        ::fillDirt
    )
}

@Serializable
data class GardenBedState(
    override var intent: Intent? = null,
    override val position: MutablePosition = MutablePosition(),
    var isFullOfDirt: Boolean = false,
    override var stored: Double = 0.0
): ResourceConsumerState {
    override val isAlive get() = true
    override val capacity get() = 200.0
    override val resource get() = Resource.Dirt
}

interface ResourceConsumerState: EntityState, ResourceInventory {
    override val capacity: Double
    override var stored: Double
    override val resource: Resource
}

interface ResourceInventory {
    val capacity: Double
    val stored: Double
    val resource: Resource?

    val isFull get () = stored >= capacity
    val isEmpty get () = stored == 0.0
    val capacityAvailable get () = capacity - stored
}