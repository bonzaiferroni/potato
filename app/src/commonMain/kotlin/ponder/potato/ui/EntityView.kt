package ponder.potato.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.RenderVectorGroup
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.Ghost
import compose.icons.tablericons.Man
import compose.icons.tablericons.QuestionMark
import kotlinx.collections.immutable.toImmutableList
import ponder.potato.LocalGame
import ponder.potato.model.game.*
import ponder.potato.model.game.entities.Imp
import ponder.potato.model.game.entities.Sprite
import ponder.potato.model.game.zones.GameState
import pondui.ui.controls.Icon

@Composable
fun EntityView(
    entityId: Long,
    boxSize: IntSize,
    viewModel: EntityViewModel = viewModel(key = entityId.toString()) { EntityViewModel(entityId) }
) {
    val state by viewModel.state.collectAsState()
    val gameState by LocalGame.current.state.collectAsState()

    val animatedX by animatePosition(state.x, state.delta, state.isTeleported)
    val animatedY by animatePosition(state.y, state.delta, state.isTeleported)
    val animatedSpirit by animateFloatAsState(state.spiritRatio)

    LaunchedEffect(gameState) {
        viewModel.update(gameState)
    }

    val myImageVector = getImage(viewModel.type)

    if (state.isVisible && !state.isTeleported && boxSize != IntSize.Zero) {
        val centerX = boxSize.width * (animatedX + BOUNDARY_X) / (BOUNDARY_X * 2)
        val centerY = boxSize.height * (animatedY + BOUNDARY_Y) / (BOUNDARY_Y * 2)
        val radiusPx = with(LocalDensity.current) { 20.dp.toPx() }

        Column(
            modifier = Modifier
                .width(20.dp)
                .graphicsLayer {
                    translationX = centerX - radiusPx
                    translationY = centerY - radiusPx
                }
        ) {
            Icon(
                imageVector = myImageVector,
                tint = Color.White,
                modifier = Modifier.fillMaxWidth()
                    .aspectRatio(1f)
            )
            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(2.dp)
                    .drawBehind {
                        drawRect(
                            color = Color.Red
                        )
                        drawRect(
                            color = Color.Green,
                            size = Size(size.width * animatedSpirit, size.height)
                        )
                    }
            )
        }
    }
}

fun getImage(type: String) = when {
    type == Sprite::class.simpleName -> TablerIcons.Ghost
    type == Imp::class.simpleName -> TablerIcons.Man
    else -> TablerIcons.QuestionMark
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