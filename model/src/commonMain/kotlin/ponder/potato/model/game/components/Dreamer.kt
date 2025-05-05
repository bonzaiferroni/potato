package ponder.potato.model.game.components

import ponder.potato.model.game.entities.EntityState
import ponder.potato.model.game.entities.StateEntity
import ponder.potato.model.game.factorValue

interface DreamerState: EntityState {
    val aetherReward: Double

    fun getReward(dreamLevel: Int) = factorValue(aetherReward.toInt(), dreamLevel, 1.5)
}

class Dreamer(
    override val entity: StateEntity<DreamerState>
) : StateComponent<DreamerState>() {
}