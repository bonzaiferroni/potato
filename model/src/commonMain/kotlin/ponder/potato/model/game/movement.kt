package ponder.potato.model.game

import ponder.potato.model.game.components.MoverState
import ponder.potato.model.game.entities.Entity
import ponder.potato.model.game.entities.StateEntity
import kotlin.math.sqrt
import kotlin.random.Random

fun Entity.moveToward(position: Position, delta: Double) {
    val potential = delta * state.speed
    val dx = position.x - state.position.x
    val dy = position.y - state.position.y
    val distance = sqrt(dx * dx + dy * dy)
    if (distance <= potential || distance == 0f) {
        state.position.x = position.x
        state.position.y = position.y
        return
    }

    val ratio = potential / distance
    state.position.x += (dx * ratio).toFloat()
    state.position.y += (dy * ratio).toFloat()
}

fun StateEntity<MoverState>.setRandomDestination(xBound: Float = BOUNDARY_X, yBound: Float = BOUNDARY_Y) {
    state.destination.x = Random.nextFloat() * (2 * xBound) - xBound
    state.destination.y = Random.nextFloat() * (2 * yBound) - yBound
    state.destination.zoneId = position.zoneId
}

fun StateEntity<*>.approach(position: Position, delta: Double, range: Int = 1): Boolean {
    if (this.zone.id != position.zoneId) return false
    val squaredDistance = this.position.squaredDistanceTo(position)
    val squaredRange = range * range
    if (squaredDistance > squaredRange) {
        this.moveToward(position, delta)
        return false
    }

    return true
}
