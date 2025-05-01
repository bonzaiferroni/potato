package ponder.potato.model.game.components

import ponder.potato.model.game.MutablePosition
import ponder.potato.model.game.entities.EntityState
import ponder.potato.model.game.entities.StateEntity
import ponder.potato.model.game.moveToward

interface MoverState: EntityState {
    val destination: MutablePosition
    override val position: MutablePosition
}

class MoverComponent(
    override val entity: StateEntity<MoverState>,
): StateComponent<MoverState>() {

    override fun update(delta: Double) {
        super.update(delta)

        if (entity.position.atPosition(state.destination)) return
        entity.moveToward(state.destination, delta)
    }
}