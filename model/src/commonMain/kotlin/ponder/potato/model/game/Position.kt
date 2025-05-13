package ponder.potato.model.game

import kotlinx.serialization.Serializable

interface Position : Point {
    val zoneId: Int
    fun atPosition(position: Position) = x == position.x && y == position.y && zoneId == position.zoneId

    fun squaredWorldDistanceTo(position: Position) = if (zoneId != position.zoneId) {
        Float.MAX_VALUE
    } else {
        squaredDistanceTo(position)
    }
}

@Serializable
data class MutablePosition(
    override var zoneId: Int = 0,
    override var x: Float = 0.0f,
    override var y: Float = 0.0f
) : Position