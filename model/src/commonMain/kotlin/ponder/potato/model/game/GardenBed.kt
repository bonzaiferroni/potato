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
): EntityState {
    override val isAlive get() = true
    val capacity get() = 1000.0
}