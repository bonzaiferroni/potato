package ponder.potato.model.game.entities

import ponder.potato.model.game.MutablePosition
import ponder.potato.model.game.components.StateComponent

class Bard(
    override val state: BardState = BardState()
): StateEntity<BardState>() {
    override val components: List<StateComponent<*>> = listOf(

    )
}

data class BardState(
    override val isAlive: Boolean = true,
    override val position: MutablePosition = MutablePosition()
): EntityState