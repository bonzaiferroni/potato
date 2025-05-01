package ponder.potato.model.game.entities

import kotlinx.serialization.Serializable
import ponder.potato.model.game.MutablePosition
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
    override val level: Int = 1,
    override var spirit: Int = 0,
    override var isAlive: Boolean = false,
    override val position: MutablePosition = MutablePosition(),
) : SpiritState, ProgressState {
    override val maxSpirit get() = factorValue(100, level, 1.2).toInt()
}