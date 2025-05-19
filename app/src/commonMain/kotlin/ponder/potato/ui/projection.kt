package ponder.potato.ui

import ponder.potato.model.game.Point
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

data class Projection(override val x: Float, override val y: Float): Point

fun projection_test(
    x: Float,
    y: Float,
    rotationDegrees: Float = 60f,
    height: Float = 1f,
    distance: Float = 6f
): Projection {
    val angle = 90f / rotationDegrees

    val projectedX = x + ((y - distance) / 6)
    val projectedY = y / angle + height
    return Projection(projectedX, projectedY)
}