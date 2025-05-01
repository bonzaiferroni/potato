package ponder.potato.model.game

import kotlinx.serialization.Serializable

interface Position : Vector {
    val zoneId: Int
    fun atPosition(position: Position) = x == position.x && y == position.y && zoneId == position.zoneId
}

data class Point(
    override val x: Float,
    override val y: Float,
): Vector

@Serializable
data class MutablePosition(
    override var zoneId: Int = 0,
    override var x: Float = 0.0f,
    override var y: Float = 0.0f
) : Position