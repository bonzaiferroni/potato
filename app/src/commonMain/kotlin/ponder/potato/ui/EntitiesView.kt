package ponder.potato.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.collections.immutable.ImmutableList
import ponder.potato.GameService
import ponder.potato.model.game.BOUNDARY
import ponder.potato.model.game.Vector2

@Composable
fun ZoneScope.EntitiesView(
    entityIds: ImmutableList<Long>
) {
    val game = GameService().game
    val entities = entityIds.mapNotNull {
        val entity = game.entities[it] ?: return@mapNotNull null
        val composition by rememberLottieComposition { entity.toLottieResource(false)}
        val painter = rememberLottiePainter(composition, iterations = Compottie.IterateForever)
        val x = entity.position.x; val y = entity.position.y
        val (xPosition, yPosition) = remember(Vector2(x, y)) { zoneToPerspective(x, y) }
        val left = zoneBoxSize.width * (xPosition + BOUNDARY) / (BOUNDARY * 2)
        val top = zoneBoxSize.height * (-yPosition + BOUNDARY) / (BOUNDARY * 2)
        DrawnEntity(painter, Vector2(left, top))
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .drawBehind {

                for (entity in entities) {
                    translate(left = entity.position.x, top = entity.position.y) {
                        with(entity.painter) {
                            draw(Size(50f, 50f))
                        }
                    }
                }
            }
    )
}

data class DrawnEntity(
    val painter: Painter,
    val position: Vector2
)
