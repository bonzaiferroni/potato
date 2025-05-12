package ponder.potato.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import ponder.potato.model.game.BOUNDARY_X
import ponder.potato.model.game.BOUNDARY_Y
import ponder.potato.model.game.Vector2

@Composable
fun ZoneScope.ZoneObject(
    x: Float,
    y: Float,
    delta: Double,
    content: @Composable () -> Unit
) {
    val (xPosition, yPosition, distance) = remember(Vector2(x, y)) { projection(x, y) }

    if (boxSize == IntSize.Zero || xPosition == Float.NaN || yPosition == Float.NaN) return

    val animatedX by animatePosition(xPosition, delta)
    val animatedY by animatePosition(-yPosition, delta) // flip y for camera space
    val animatedScale by animatePosition(12 / distance, delta)

    val centerX = boxSize.width * (animatedX + BOUNDARY_X) / (BOUNDARY_X * 2)
    val centerY = boxSize.height * (animatedY + BOUNDARY_Y) / (BOUNDARY_Y * 2)
    val entitySize = 50f
    val radius = entitySize / 2
    val radiusPx = with(LocalDensity.current) { radius.dp.toPx() }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width((radius * 2).dp)
            .zIndex(animatedScale)
            .graphicsLayer {
                translationX = centerX - radiusPx
                translationY = centerY - radiusPx
                scaleX = animatedScale
                scaleY = animatedScale
            }
    ) {
        content()
    }
}

@Stable
class ZoneScope(
    val boxSize: IntSize
)