package ponder.potato.ui

import ponder.potato.model.game.Point
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

/**
 * Represents the projected 2D point and its distance from the camera.
 *
 * @property x The x-coordinate in the projected 2D space.
 * @property y The y-coordinate in the projected 2D space.
 * @property distance The distance from the camera to the original 3D point.
 */
data class ProjectionPersp(override val x: Float, override val y: Float, val distance: Float): Point

/**
 * Projects a 3D point (x, y, 0) onto a 2D plane with perspective, considering camera position and rotation.
 * Assumes the original points lie on the z=0 plane.
 *
 * @param pointX The x-coordinate of the original 3D point.
 * @param pointY The y-coordinate of the original 3D point.
 * @param cameraHeight The height of the camera above the z=0 plane.
 * @param cameraDistance The distance of the camera from the origin in the XY plane.
 * @param cameraRotationXDegrees The rotation of the camera around the x-axis in degrees. 0 degrees means looking straight ahead along the negative Z axis (after translation).
 * @param fieldOfViewDegrees The vertical field of view of the camera in degrees.
 * @return A `Projection` object containing the projected x and y coordinates and the distance from the camera.
 */
fun projection_perspective(
    pointX: Float,
    pointY: Float,
    cameraHeight: Float = 8.5f,
    cameraDistance: Float = 3f,
    cameraRotationXDegrees: Float = 20f,
    fieldOfViewDegrees: Float = 18f
): ProjectionPersp {
    // Convert degrees to radians using Kotlin's standard library
    val cameraRotationXRad = cameraRotationXDegrees * (PI / 180.0f).toFloat()
    val fieldOfViewRad = fieldOfViewDegrees * (PI / 180.0f).toFloat()

    // Assume the original point is at (pointX, pointY, 0) in world space
    val worldPoint = floatArrayOf(pointX, pointY, 0f)

    // Camera position. Assume camera is at (0, -cameraDistance, cameraHeight) in world space
    val cameraPosition = floatArrayOf(0f, -cameraDistance, cameraHeight)

    // 1. Translate the world point by the inverse of the camera's world position to get to view space.
    val translatedPointX = worldPoint[0] - cameraPosition[0]
    val translatedPointY = worldPoint[1] - cameraPosition[1]
    val translatedPointZ = worldPoint[2] - cameraPosition[2]
    val translatedPoint = floatArrayOf(translatedPointX, translatedPointY, translatedPointZ)

    // 2. Apply the inverse of the camera's world rotation to get to camera space.
    //    Camera is rotated by cameraRotationXDegrees around its X axis.
    //    Inverse rotation is -cameraRotationXDegrees around the X axis.
    val cosTheta = cos(-cameraRotationXRad)
    val sinTheta = sin(-cameraRotationXRad)

    val cameraSpaceX = translatedPoint[0]
    val cameraSpaceY = translatedPoint[1] * cosTheta - translatedPoint[2] * sinTheta
    val cameraSpaceZ = translatedPoint[1] * sinTheta + translatedPoint[2] * cosTheta
    // In a standard perspective projection_perspective setup (like OpenGL), the camera looks down the negative Z axis.
    // Points in front of the camera will have negative Z in camera space.

    // Apply perspective projection_perspective
    var projectedX: Float
    var projectedY: Float
    val depth = abs(cameraSpaceZ) // Depth is the distance along the camera's Z-axis

    // To avoid division by zero or projecting points behind the camera in the standard convention
    if (cameraSpaceZ >= 0) {
        // Point is behind or at the camera's Z plane in this convention, cannot project meaningfully
        projectedX = Float.NaN
        projectedY = Float.NaN

    } else {
        // Project onto the image plane. The distance to the projection_perspective plane is related to the focal length.
        // focalLength = 1 / tan(fieldOfView / 2)
        val focalLength = 1.0f / tan(fieldOfViewRad / 2.0f)
        val aspectRatio = 1.0f // Assuming square aspect ratio for simplicity. Adjust if needed.

        // Projected coordinates using similar triangles: projected_x / focalLength = cameraSpaceX / cameraSpaceZ
        // Note: cameraSpaceZ is negative, so we use -cameraSpaceZ for the distance term in similar triangles
        projectedX = cameraSpaceX / (-cameraSpaceZ) * focalLength * aspectRatio
        projectedY = cameraSpaceY / (-cameraSpaceZ) * focalLength
    }

    return ProjectionPersp(projectedX, projectedY, depth)
}