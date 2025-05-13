package ponder.potato.model.game

import kotlin.math.sqrt

interface Point {
    val x: Float
    val y: Float
}

fun Point.squaredDistanceTo(point: Point): Float {
    val dx = point.x - x
    val dy = point.y - y
    return dx * dx + dy * dy
}

fun Point.distanceTo(point: Point) = sqrt(this.squaredDistanceTo(point))

data class Vector2(
    override val x: Float,
    override val y: Float
): Point