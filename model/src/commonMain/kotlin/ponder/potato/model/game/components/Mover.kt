package ponder.potato.model.game.components

import ponder.potato.model.game.BOUNDARY_X
import ponder.potato.model.game.BOUNDARY_Y
import ponder.potato.model.game.MutablePosition
import ponder.potato.model.game.entities.EntityState
import ponder.potato.model.game.entities.Intent
import ponder.potato.model.game.entities.StateEntity
import ponder.potato.model.game.moveTo
import ponder.potato.model.game.moveToward
import kotlin.random.Random

interface MoverState: EntityState {
    val destination: MutablePosition
    override val position: MutablePosition
}

class Mover(
    override val entity: StateEntity<MoverState>,
): StateComponent<MoverState>() {

    override fun update(delta: Double) {
        if (!entity.hasIntent(Intent.Travel)) return

        if (entity.position.atPosition(state.destination)) {
            state.intent = null
            return
        }

        entity.moveTo(state.destination, delta)
    }
}

fun StateEntity<MoverState>.setRandomDestination(xBound: Float = BOUNDARY_X, yBound: Float = BOUNDARY_Y) {
    state.destination.x = Random.nextFloat() * (2 * xBound) - xBound
    state.destination.y = Random.nextFloat() * (2 * yBound) - yBound
    state.destination.zoneId = position.zoneId
}

fun StateEntity<MoverState>.travelTo(x: Float, y: Float, zoneId: Int) {
    state.destination.x = x
    state.destination.y = y
    state.destination.zoneId = zoneId
    state.intent = Intent.Travel
}