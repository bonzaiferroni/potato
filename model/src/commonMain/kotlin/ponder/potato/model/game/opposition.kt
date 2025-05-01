package ponder.potato.model.game

import ponder.potato.model.game.components.OpposerState
import ponder.potato.model.game.components.SpiritState
import ponder.potato.model.game.entities.Entity
import ponder.potato.model.game.entities.StateEntity

@Suppress("UNCHECKED_CAST")
fun StateEntity<OpposerState>.oppose(target: StateEntity<SpiritState>) {
    val distance = this.position.squaredWorldDistanceTo(target.position)
    if (distance > 1) return

    target.state.spirit -= this.state.power
    this.showEffect { OpposeEffect(target, this.state.power) }
    target.showEffect { Despirit(this.state.power) }
    (target as? StateEntity<OpposerState>)?.takeIf { it.state.oppositionId == null }?.let {
        it.state.oppositionId = target.id
    }
}