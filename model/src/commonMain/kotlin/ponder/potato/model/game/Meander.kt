package ponder.potato.model.game

import kabinet.utils.diceRoll
import kotlin.random.Random

class Meander(
    override val entity: StateEntity<MoverState>
) : StateComponent<MoverState>() {

    override fun init() {
        super.init()
        entity.setRandomDestination()
    }

    override fun update(delta: Double) {
        if (state.intent != null) return

        val squaredDistance = entity.position.squaredDistanceTo(state.destination)
        if (squaredDistance > 0) {
            entity.moveToward(state.destination, delta)
        } else {
            if (Random.diceRoll(6) > 5) {
                entity.setRandomDestination()
            }
        }
    }
}