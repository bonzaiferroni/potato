package ponder.potato.model.game.components

import ponder.potato.model.game.entities.Entity
import ponder.potato.model.game.entities.EntityState
import ponder.potato.model.game.entities.StateEntity

interface Mover : Entity {
    override val state: MoverState
}

interface MoverState: EntityState {
    val target: Position
    val position: Position
}

class MoverComponent(
    entity: StateEntity<*>,
    val target: PositionState,
    val position: PositionState,
): Component(entity) {

    val targets = mutableListOf<Position>()

    override fun update(delta: Double) {
        super.update(delta)

        if (position.atPosition(target)) return
        position.moveToward(target, delta)
    }
}