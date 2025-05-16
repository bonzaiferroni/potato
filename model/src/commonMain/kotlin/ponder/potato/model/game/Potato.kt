package ponder.potato.model.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class Potato(
    override val state: PotatoState = PotatoState()
) : StateEntity<PotatoState>() {
    override val components = emptyList<StateComponent<*>>()
}

@Serializable
@SerialName("potato")
data class PotatoState(
    override var level: Int = 1,
    override val position: MutablePosition = MutablePosition(),
    override var intent: Intent? = null,
) : EntityState, LevelState, DreamerState, EntityStorageState {
    override val isAlive get() = true
    override val aetherReward get() = factorValue(30, level, 1.2)
    override val capacity get() = factorValue(1000, level, 1.2)
    override val resource get() = Resource.Aether
}