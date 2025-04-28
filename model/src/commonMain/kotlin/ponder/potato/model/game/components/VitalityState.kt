package ponder.potato.model.game.components

import ponder.potato.model.game.entities.StateEntity

interface VitalityState {
    val health: Int
    val maxHealth: Int
}

class VitalityComponent(
    entity: StateEntity<*>,
)