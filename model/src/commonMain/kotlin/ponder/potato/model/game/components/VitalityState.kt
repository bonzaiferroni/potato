package ponder.potato.model.game.components

import ponder.potato.model.game.entities.EntityState
import ponder.potato.model.game.entities.StateEntity

interface VitalityState: EntityState {
    var health: Int
    val maxHealth: Int
    override val isAlive get() = health > 0
}

class VitalityComponent(
    entity: StateEntity<VitalityState>,
): StateComponent<VitalityState>(entity) {
    override fun init() {
        super.init()
        state.health = state.maxHealth
    }
}