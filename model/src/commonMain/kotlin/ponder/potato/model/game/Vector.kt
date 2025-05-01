package ponder.potato.model.game

import kotlin.math.sqrt

interface Vector {
    val x: Float
    val y: Float
}

fun Vector.squaredDistanceTo(vector: Vector): Float {
    val dx = vector.x - x
    val dy = vector.y - y
    return dx * dx + dy * dy
}

fun Vector.distanceTo(vector: Vector) = sqrt(this.squaredDistanceTo(vector))