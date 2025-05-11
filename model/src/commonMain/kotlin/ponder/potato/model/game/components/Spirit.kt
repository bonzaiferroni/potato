package ponder.potato.model.game.components

import ponder.potato.model.game.Despirit
import ponder.potato.model.game.entities.Entity
import ponder.potato.model.game.entities.EntityState
import ponder.potato.model.game.entities.SpriteState
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
        state.spirit = state.maxSpirit
    }
}

val Entity.spirit get() = (this.state as? SpiritState)?.spirit ?: 0
val Entity.maxSpirit get() = (this.state as? SpiritState)?.maxSpirit ?: 0
val Entity.spiritFull get() = spirit >= maxSpirit
fun Entity.despirit(amount: Int) {
    (this.state as? SpiritState)?.let {
        it.spirit -= minOf(amount, it.spirit)
        this.showEffect { Despirit(amount) }
    }
}