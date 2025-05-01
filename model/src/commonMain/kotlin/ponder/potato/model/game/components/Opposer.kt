package ponder.potato.model.game.components

import ponder.potato.model.game.entities.*

interface OpposerState: EntityState {
    var oppositionId: Long?
    val power: Int
}

class Opposer(
    override val entity: StateEntity<OpposerState>
): StateComponent<OpposerState>() {
}