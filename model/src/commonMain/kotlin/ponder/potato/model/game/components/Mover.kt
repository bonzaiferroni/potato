package ponder.potato.model.game.components

import ponder.potato.model.game.entities.EntityState
import ponder.potato.model.game.entities.StateEntity

interface MoverState: PositionState {
    val target: MutablePosition
    override val position: MutablePosition
}

class MoverComponent(
    entity: StateEntity<MoverState>,
): StateComponent<MoverState>(entity) {

    val targets = mutableListOf<Position>()

    override fun update(delta: Double) {
        super.update(delta)

        if (state.position.atPosition(state.target)) return
        state.position.moveToward(state.target, delta)
    }
}