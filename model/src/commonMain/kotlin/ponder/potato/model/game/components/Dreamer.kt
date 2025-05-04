package ponder.potato.model.game.components

import ponder.potato.model.game.entities.EntityState
import ponder.potato.model.game.entities.StateEntity

interface DreamerState: EntityState {
    val aetherReward: Double
}

class Dreamer(
    override val entity: StateEntity<DreamerState>
) : StateComponent<DreamerState>() {
}