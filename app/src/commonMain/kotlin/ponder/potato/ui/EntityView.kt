package ponder.potato.ui

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
    viewModel: EntityViewModel = viewModel { EntityViewModel(entityId) }
) {
    val state by viewModel.state.collectAsState()
    val gameState by LocalGame.current.state.collectAsState()

    LaunchedEffect(gameState) {
        viewModel.update(gameState)
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.Blue.copy(.1f))
            .drawBehind {
                val radiusPx = 10.dp.toPx()
                val centerX = size.width * (state.x + BOUNDARY_X) / (BOUNDARY_X * 2)
                val centerY = size.height * (state.y + BOUNDARY_Y) / (BOUNDARY_Y * 2)
                drawCircle(
                    color = state.color, // or whatever hue ye fancy
                    radius = radiusPx,
                    center = Offset(centerX, centerY)
                )
            }
    )
}