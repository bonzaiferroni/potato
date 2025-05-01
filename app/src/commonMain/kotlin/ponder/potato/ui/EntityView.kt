package ponder.potato.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.collections.immutable.toImmutableList
import ponder.potato.LocalGame
import ponder.potato.model.game.*
import ponder.potato.model.game.zones.GameState

@Composable
fun EntityView(
    entityId: Long,
    viewModel: EntityViewModel = viewModel(key = entityId.toString()) { EntityViewModel(entityId) }
) {
    val state by viewModel.state.collectAsState()
    val gameState by LocalGame.current.state.collectAsState()

    val animatedX by animatePosition(state.x, state.delta, state.isTeleported)
    val animatedY by animatePosition(state.y, state.delta, state.isTeleported)

    LaunchedEffect(gameState) {
        viewModel.update(gameState)
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .drawBehind {
                if (!state.isVisible || state.isTeleported) return@drawBehind
                val radiusPx = 10.dp.toPx()
                val centerX = size.width * (animatedX + BOUNDARY_X) / (BOUNDARY_X * 2)
                val centerY = size.height * (animatedY + BOUNDARY_Y) / (BOUNDARY_Y * 2)
                drawCircle(
                    color = state.color, // or whatever hue ye fancy
                    radius = radiusPx,
                    center = Offset(centerX, centerY)
                )
            }
    )
}

@Composable
fun animatePosition(
    value: Float,
    delta: Double,
    snap: Boolean
) = animateFloatAsState(
    targetValue = value,
    animationSpec = if (snap) snap() else tween((delta * 1000).toInt(), easing = LinearEasing)
)