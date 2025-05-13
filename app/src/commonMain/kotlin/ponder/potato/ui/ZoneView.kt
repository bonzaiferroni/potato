package ponder.potato.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.viewmodel.compose.viewModel
import ponder.potato.LocalGame
import ponder.potato.ZoneRoute
import ponder.potato.model.game.BOUNDARY_X
import ponder.potato.model.game.BOUNDARY_Y
import ponder.potato.model.game.Point
import ponder.potato.model.game.Vector2
import ponder.potato.model.game.Zone
import pondui.ui.controls.NavButton
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
        verticalArrangement = Pond.ruler.columnTight,
    ) {
        Box(
            modifier = modifier.fillMaxWidth()
                .aspectRatio(1.5f)
                .clipToBounds()
                .onGloballyPositioned { coordinates ->
                    boxSize = coordinates.size
                }
                .drawBehind {
                    drawRect(Color.DarkGray.copy(.1f))

                    val topWidth = size.width * 0.50f
                    val bottomWidth = size.width
                    val totalHeight = size.height
                    val topHeight = totalHeight * 0.40f
                    val leftOffset = (bottomWidth - topWidth) / 2f
                    val topLeft = topLeftProjection.toZoneViewSpace(boxSize)
                    val topRight = topRightProjection.toZoneViewSpace(boxSize)
                    val bottomLeft = bottomLeftProjection.toZoneViewSpace(boxSize)
                    val bottomRight = bottomRightProjection.toZoneViewSpace(boxSize)

                    val path = Path().apply {
                        moveTo(topLeft.x, topLeft.y)                    // Top left
                        lineTo(topRight.x, topRight.y)         // Top right
                        lineTo(bottomRight.x, bottomRight.y)                 // Bottom right
                        lineTo(bottomLeft.x, bottomLeft.y)                          // Bottom left
                        close()
                    }

                    drawPath(path = path,  color = Color.Blue.copy(.1f))

                    val lineColor = Color.Green.copy(.1f)
                    for (x in (-BOUNDARY_X.toInt()..BOUNDARY_X.toInt() + 1)) {
                        val start = projection_perspective(x.toFloat() - .5f, -BOUNDARY_Y - .5f).toZoneViewSpace(boxSize)
                        val end = projection_perspective(x.toFloat() - .5f, BOUNDARY_Y + .5f).toZoneViewSpace(boxSize)
                        drawLine(lineColor, Offset(start.x, start.y), Offset(end.x, end.y))
                    }

                    for (y in (-BOUNDARY_Y.toInt()..BOUNDARY_Y.toInt() + 1)) {
                        val start = projection_perspective(-BOUNDARY_X - .5f, y.toFloat() - .5f).toZoneViewSpace(boxSize)
                        val end = projection_perspective(BOUNDARY_X + .5f, y.toFloat() - .5f).toZoneViewSpace(boxSize)
                        drawLine(lineColor, Offset(start.x, start.y), Offset(end.x, end.y))
                    }
                }
        ) {
            val zoneScope = ZoneScope(boxSize)
            for (entityId in state.entityIds) {
                key(entityId) {
                    zoneScope.EntityView(entityId, state.fullVisibility)
                }
            }
            for (exit in state.exits) {
                zoneScope.ZoneObject(exit.x, exit.y, 1.0) {
                    NavButton(exit.zoneId.toString(), modifier = Modifier.alpha(.3f)) { ZoneRoute(exit.zoneId) }
                }
            }
        }
    }
}

val topLeftProjection = projection_perspective(-BOUNDARY_X - .5f, BOUNDARY_Y + .5f)
val topRightProjection = projection_perspective(BOUNDARY_X + .5f, BOUNDARY_Y + .5f)
val bottomLeftProjection = projection_perspective(-BOUNDARY_X - .5f, -BOUNDARY_Y - .5f)
val bottomRightProjection = projection_perspective(BOUNDARY_X + .5f, -BOUNDARY_Y - .5f)

fun Point.toZoneViewSpace(boxSize: IntSize) = Vector2(
    x = boxSize.width * (x + BOUNDARY_X) / (BOUNDARY_X * 2),
    y = boxSize.height * (-y + BOUNDARY_Y) / (BOUNDARY_Y * 2),
)