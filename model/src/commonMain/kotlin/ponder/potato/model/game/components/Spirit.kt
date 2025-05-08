package ponder.potato.model.game.components

import ponder.potato.model.game.entities.EntityState
import ponder.potato.model.game.entities.StateEntity

interface SpiritState: EntityState {
    var spirit: Int
    val maxSpirit: Int
    override val isAlive get() = spirit > 0
}

class Spirit(
    override val entity: StateEntity<SpiritState>,
): StateComponent<SpiritState>() {
    override fun init() {
        super.init()
        state.spirit = state.maxSpirit
    }
}

val StateEntity<*>.spirit get() = (this.state as? SpiritState)?.spirit ?: 0
val StateEntity<*>.maxSpirit get() = (this.state as? SpiritState)?.maxSpirit ?: 0
val StateEntity<*>.spiritFull get() = spirit >= maxSpirit