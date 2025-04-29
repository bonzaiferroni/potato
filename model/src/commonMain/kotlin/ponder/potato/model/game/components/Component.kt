package ponder.potato.model.game.components

import ponder.potato.model.game.entities.Entity
import ponder.potato.model.game.entities.EntityState
import ponder.potato.model.game.entities.StateEntity
import ponder.potato.model.game.zones.Game
import ponder.potato.model.game.zones.Zone

interface Component {
    val state: EntityState
    val entity: Entity
    val zone: Zone
    val game: Game
}

abstract class StateComponent<T: EntityState>(
    override val entity: StateEntity<T>,
): Component {
    override val state get() = entity.state as? T ?: error("invalid state")
    override val zone get() = entity.zone
    override val game get() = entity.game

    open fun init() { }
    open fun update(delta: Double) { }
}