package ponder.potato.model.game.components

import ponder.potato.model.game.entities.StateEntity
import ponder.potato.model.game.moveToward
import ponder.potato.model.game.setRandomDestination
import ponder.potato.model.game.squaredDistanceTo

class Meander(
    override val entity: StateEntity<MoverState>
) : StateComponent<MoverState>() {

    override fun init() {
        super.init()
        entity.setRandomDestination()
    }

    override fun update(delta: Double) {
        super.update(delta)

        val squaredDistance = entity.position.squaredDistanceTo(state.destination)
        if (squaredDistance > 0) {
            entity.moveToward(state.destination, delta)
        } else {
            entity.setRandomDestination()
        }
    }
}