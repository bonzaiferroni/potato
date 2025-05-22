package ponder.potato.ui

import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ponder.potato.LocalGameHost
import ponder.potato.ZoneRoute
import ponder.potato.model.game.BOUNDARY
import ponder.potato.model.game.Point
import ponder.potato.model.game.Vector2
import ponder.potato.model.game.Zone
import pondui.ui.controls.NavButton
import pondui.ui.theme.Pond
import kotlin.reflect.KClass

@Composable
fun <T : Zone> ZoneView(
    zoneClass: KClass<T>,
    modifier: Modifier = Modifier,
    highlightedId: Long?,
    onHoverChange: (Long, Boolean) -> Unit,
    onClick: (Long?) -> Unit,
    viewModel: ZoneViewModel<T> = viewModel { ZoneViewModel(zoneClass) }
) {
    val state by viewModel.state.collectAsState()
    val gameState by LocalGameHost.current.state.collectAsState()

    LaunchedEffect(gameState) {
        viewModel.update(gameState)
    }

    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Pond.ruler.columnTight,
    ) {
        val textMeasurer = rememberTextMeasurer()
        Box(
            modifier = modifier.fillMaxWidth()
                .aspectRatio(1.5f)
                .clipToBounds()
                .pointerInput(Unit) {
//                    awaitPointerEventScope {
//                        while (true) {
//                            val event = awaitPointerEvent()
//                            val position = event.changes.first().position
//                            println("Hover at: $position")
//                        }
//                    }
                    detectTapGestures { offset ->
                        val canvasPoint = offset.toCanvasSpace(boxSize)
                        val zonePoint = perspectiveToZone(canvasPoint.x, canvasPoint.y)
                        onClick(null)
                        // println(zonePoint)
                    }
                }
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

                    drawPath(path = path, color = Color.Blue.copy(.1f))

                    val lineColor = Color.Green.copy(.1f)
                    for (x in (-BOUNDARY.toInt()..BOUNDARY.toInt() + 1)) {
                        val start = projection(x.toFloat() - .5f, -BOUNDARY - .5f).toZoneViewSpace(boxSize)
                        val end = projection(x.toFloat() - .5f, BOUNDARY + .5f).toZoneViewSpace(boxSize)
                        drawLine(lineColor, Offset(start.x, start.y), Offset(end.x, end.y))
                    }

                    for (y in (-BOUNDARY.toInt()..BOUNDARY.toInt() + 1)) {
                        val start = projection(-BOUNDARY - .5f, y.toFloat() - .5f).toZoneViewSpace(boxSize)
                        val end = projection(BOUNDARY + .5f, y.toFloat() - .5f).toZoneViewSpace(boxSize)
                        drawLine(lineColor, Offset(start.x, start.y), Offset(end.x, end.y))
                    }

                    for (xIndex in -1..1) {
                        for (yIndex in -1..1) {
                            val x = xIndex * 5f;
                            val y = yIndex * 5f
                            val offset = projection(x, y).toZoneViewSpace(boxSize).let { Offset(it.x, it.y) }
                            val textLayoutResult = textMeasurer.measure(
                                text = AnnotatedString("(${x.toInt()},${y.toInt()})"),
                                style = TextStyle(fontSize = 12.sp, color = Color.White)
                            )
                            drawCircle(Color.White, radius = 1f, center = offset)
                            drawText(textLayoutResult, topLeft = offset)
                        }
                    }
                }
        ) {
            val zoneScope = ZoneScope(boxSize)
            for (entityId in state.entityIds) {
                key(entityId) {
                    zoneScope.EntityView(
                        entityId = entityId,
                        zoneId = viewModel.zone.id,
                        fullVisibility = state.fullVisibility,
                        isHighlighted = entityId == highlightedId,
                        onHoverChange = onHoverChange,
                        onClick = onClick,
                    )
                }
            }
//             zoneScope.EntitiesView(state.entityIds)
            for (exit in state.exits) {
                zoneScope.ZoneObject(exit.x, exit.y, 1.0) {
                    NavButton(exit.zoneId.toString(), modifier = Modifier.alpha(.3f)) { ZoneRoute(exit.zoneId) }
                }
            }
            FpsIndicator(modifier = Modifier.align(Alignment.TopEnd))
        }
    }
}

val topLeftProjection = projection(-BOUNDARY - .5f, BOUNDARY + .5f)
val topRightProjection = projection(BOUNDARY + .5f, BOUNDARY + .5f)
val bottomLeftProjection = projection(-BOUNDARY - .5f, -BOUNDARY - .5f)
val bottomRightProjection = projection(BOUNDARY + .5f, -BOUNDARY - .5f)

fun Point.toZoneViewSpace(boxSize: IntSize) = Vector2(
    x = boxSize.width * (x + BOUNDARY) / (BOUNDARY * 2),
    y = boxSize.height * (-y + BOUNDARY) / (BOUNDARY * 2),
)

fun Offset.toCanvasSpace(boxSize: IntSize): Vector2 {
    val xZone = (x / boxSize.width) * (BOUNDARY * 2) - BOUNDARY
    val yZone = -((y / boxSize.height) * (BOUNDARY * 2) - BOUNDARY)
    return Vector2(xZone, yZone)
}

fun projection(x: Float, y: Float) = zoneToPerspective(x, y)