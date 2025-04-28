package ponder.potato.model.game.components

import kotlinx.serialization.Serializable
import ponder.potato.model.game.Point
import ponder.potato.model.game.entities.StateEntity
import kotlin.math.sqrt

interface Position : Point {
    val zoneId: String

    fun atPosition(position: Position) = x == position.x && y == position.y && zoneId == position.zoneId
}

class PositionComponent(
    entity: StateEntity<*>,
    val state: PositionState
) : Component(entity), Position {
    override val x get() = state.x
    override val y get() = state.y
    override val zoneId get() = state.zoneId
}

@Serializable
data class PositionState(
    override var zoneId: String = "",
    override var x: Float = 0.0f,
    override var y: Float = 0.0f
) : Position {

    fun moveToward(position: Position, delta: Double, speed: Double = 1.0) {
        val potential = delta * speed
        val dx = position.x - x
        val dy = position.y - y
        val distance = sqrt(dx * dx + dy * dy)
        if (distance <= potential || distance == 0f) {
            x = position.x
            y = position.y
            return
        }

        val ratio = potential / distance
        x = (dx * ratio).toFloat()
        y = (dy * ratio).toFloat()
    }
}