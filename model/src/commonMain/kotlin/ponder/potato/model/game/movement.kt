package ponder.potato.model.game

import ponder.potato.model.game.entities.Entity
import kotlin.math.sqrt
import kotlin.random.Random

fun Entity.moveToward(position: Position, delta: Double, speed: Double = 1.0) {
    val potential = delta * speed
    val dx = position.x - state.position.x
    val dy = position.y - state.position.y
    val distance = sqrt(dx * dx + dy * dy)
    if (distance <= potential || distance == 0f) {
        state.position.x = position.x
        state.position.y = position.y
        return
    }

    val ratio = potential / distance
    state.position.x = (dx * ratio).toFloat()
    state.position.y = (dy * ratio).toFloat()
}

fun Entity.setRandomDestination(xBound: Float = BOUNDARY_X, yBound: Float = BOUNDARY_Y) {
    state.position.x = Random.nextFloat() * (2 * xBound) - xBound
    state.position.y = Random.nextFloat() * (2 * yBound) - yBound
}