package ponder.potato.model.game.entities

import kotlinx.serialization.Serializable
import ponder.potato.model.game.MutablePosition
import ponder.potato.model.game.components.AetherStorageState
import ponder.potato.model.game.components.DreamerState
import ponder.potato.model.game.components.SpiritState
import ponder.potato.model.game.components.StateComponent
import ponder.potato.model.game.factorValue

class Potato(
    override val state: PotatoState = PotatoState()
) : StateEntity<PotatoState>() {
    override val components = emptyList<StateComponent<*>>()
}

@Serializable
data class PotatoState(
    override var level: Int = 1,
    override val position: MutablePosition = MutablePosition(),
) : EntityState, ProgressState, DreamerState, AetherStorageState {
    override val isAlive get() = true
    override val aetherReward get() = factorValue(30, level, 1.2)
    override val aetherStorage get() = factorValue(1000, level, 1.2)
}