package ponder.potato.model.game.components

import ponder.potato.model.game.entities.StateEntity

abstract class Component(
    val entity: StateEntity<*>
) {
    internal open fun update(delta: Double) { }
}