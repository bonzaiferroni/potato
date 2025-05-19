package ponder.potato.ui

import ponder.potato.model.game.Vector2
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

fun zoneToPerspective(
    pointX: Float,
    pointY: Float,
    cameraHeight: Float = 8.5f,
    cameraDistance: Float = 3f,
    cameraRotationXDegrees: Float = 20f,
    fieldOfViewDegrees: Float = 18f
): Vector2 {
    val radX = -cameraRotationXDegrees * (PI.toFloat() / 180f)
    val fovRad = fieldOfViewDegrees * (PI.toFloat() / 180f)

    val cosX = cos(radX)
    val sinX = sin(radX)

    val dx = pointX
    val dy = pointY + cameraDistance
    val dz = -cameraHeight

    val camY = dy * cosX - dz * sinX
    val camZ = dy * sinX + dz * cosX

    if (camZ >= 0f) return Vector2(Float.NaN, Float.NaN)

    val focal = 1f / tan(fovRad / 2f)
    val projX = dx / -camZ * focal
    val projY = camY / -camZ * focal

    return Vector2(projX, projY)
}

fun perspectiveToZone(
    canvasX: Float,
    canvasY: Float,
    cameraHeight: Float = 8.5f,
    cameraDistance: Float = 3f,
    cameraRotationXDegrees: Float = 20f,
    fieldOfViewDegrees: Float = 18f
): Vector2 {
    val radX = -cameraRotationXDegrees * (PI.toFloat() / 180f)
    val fovRad = fieldOfViewDegrees * (PI.toFloat() / 180f)
    val cosX = cos(radX)
    val sinX = sin(radX)
    val focal = 1f / tan(fovRad / 2f)

    val h = cameraHeight
    val d = cameraDistance
    val u = canvasX
    val v = canvasY

    val num = h * (v * cosX - focal * sinX)
    val den = focal * cosX + v * sinX
    if (den == 0f) return Vector2(Float.NaN, Float.NaN)

    val a = num / den                // A = pointY + D
    val zoneY = a - d
    val camZNeg = h * cosX - a * sinX
    val zoneX = u * camZNeg / focal

    return Vector2(zoneX, zoneY)
}