package ponder.potato.model.game.components

import ponder.potato.model.game.entities.StateEntity
import ponder.potato.model.game.moveToward
import ponder.potato.model.game.setRandomDestination

class Meander(
    override val entity: StateEntity<MoverState>
) : StateComponent<MoverState>() {

    override fun update(delta: Double) {
        super.update(delta)

        if (entity.position.atPosition(state.destination)) {
            entity.setRandomDestination()
        } else {
            entity.moveToward(state.destination, delta)
        }
    }
}