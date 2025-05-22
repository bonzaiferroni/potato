package ponder.potato.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import ponder.potato.model.game.BOUNDARY
import ponder.potato.model.game.Vector2

@Composable
fun ZoneScope.ZoneObject(
    x: Float,
    y: Float,
    delta: Double,
    content: @Composable () -> Unit
) {
    val (xPosition, yPosition) = remember(Vector2(x, y)) { projection_perspective(x, y) }
    val distance = y + BOUNDARY

    if (zoneBoxSize == IntSize.Zero || xPosition == Float.NaN || yPosition == Float.NaN) return

    val animatedX by animatePosition(xPosition, delta)
    val animatedY by animatePosition(-yPosition, delta) // flip y for camera space
    val animatedScale by animatePosition(1f - y / BOUNDARY * .25f, delta)

    val centerX = zoneBoxSize.width * (animatedX + BOUNDARY) / (BOUNDARY * 2)
    val centerY = zoneBoxSize.height * (animatedY + BOUNDARY) / (BOUNDARY * 2)
    val entitySize = zoneBoxSize.width / 8
    val radius = entitySize / 2
    val radiusPx = with(LocalDensity.current) { radius.dp.toPx() }

    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .width((radius * 2).dp)
            .zIndex(animatedScale)
            .onGloballyPositioned { coordinates ->
                boxSize = coordinates.size
            }
             .offset { IntOffset((centerX - boxSize.width / 2).toInt(), (centerY - boxSize.height).toInt())}
            // .background(Color.Red.copy(.1f))
            .graphicsLayer {
//                 transformOrigin = TransformOrigin(0.5f, 1f) // Bottom center as pivot
//                 translationX = centerX - radiusPx
//                 translationY = centerY - boxSize.height
                scaleX = animatedScale
                scaleY = animatedScale
            }
    ) {
        content()
    }
}

@Stable
class ZoneScope(
    val zoneBoxSize: IntSize,
)