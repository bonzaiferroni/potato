package ponder.potato.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.viewmodel.compose.viewModel
import ponder.potato.LocalGame
import ponder.potato.model.game.zones.Zone
import pondui.ui.controls.Text
import pondui.ui.core.PondApp
import pondui.ui.theme.Pond
import kotlin.reflect.KClass

@Composable
fun <T: Zone> ZoneView(
    zoneClass: KClass<T>,
    modifier: Modifier = Modifier,
    viewModel: ZoneViewModel<T> = viewModel { ZoneViewModel(zoneClass) }
) {
    val state by viewModel.state.collectAsState()
    val gameState by LocalGame.current.state.collectAsState()

    LaunchedEffect(gameState) {
        viewModel.update(gameState)
    }

    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Pond.ruler.columnTight
    ) {
        Text(if (state.fullVisibility) state.name else "?")
        Box(
            modifier = modifier.fillMaxWidth()
                .aspectRatio(2f)
                .clipToBounds()
                .onGloballyPositioned { coordinates ->
                    boxSize = coordinates.size
                }
                .drawBehind {
                    val topWidth = size.width * 0.50f
                    val bottomWidth = size.width
                    val totalHeight = size.height
                    val topHeight = totalHeight * 0.40f
                    val leftOffset = (bottomWidth - topWidth) / 2f

                    val path = Path().apply {
                        moveTo(leftOffset, topHeight)                    // Top left
                        lineTo(leftOffset + topWidth, topHeight)         // Top right
                        lineTo(bottomWidth, totalHeight)                 // Bottom right
                        lineTo(0f, totalHeight)                          // Bottom left
                        close()
                    }

                    drawPath(path = path,  color = Color.Blue.copy(.1f))
                }
        ) {
            for (entityId in state.entityIds) {
                key(entityId) {
                    EntityView(entityId, state.fullVisibility, boxSize)
                }
            }
        }
    }
}