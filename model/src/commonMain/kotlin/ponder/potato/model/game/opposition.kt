package ponder.potato.model.game

import ponder.potato.model.game.components.OpposerState
import ponder.potato.model.game.components.SpiritState
import ponder.potato.model.game.entities.Entity
import ponder.potato.model.game.entities.StateEntity

fun StateEntity<OpposerState>.oppose(target: StateEntity<SpiritState>) {
    target.state.spirit -= this.state.power
}