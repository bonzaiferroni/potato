package ponder.potato.ui

import androidx.compose.ui.graphics.Color
import ponder.potato.GameService
import ponder.potato.model.game.entities.Sprite
import ponder.potato.model.game.zones.GameState
import pondui.ui.core.StateModel

class EntityViewModel(
    val entityId: Long,
    val gameService: GameService = GameService()
): StateModel<EntityViewState>(EntityViewState()) {
    val game = gameService.game
    val entity get() = game.entities[entityId]

    fun update(gameState: GameState) {
        entity?.let { e ->
            setState { it.copy(
                x = e.position.x,
                y = e.position.y,
                isVisible = true,
                delta = gameState.delta,
                color = when {
                    e is Sprite -> Color.Cyan
                    else -> Color.Magenta
                }
            ) }
        }
    }
}

data class EntityViewState(
    val x: Float = 0f,
    val y: Float = 0f,
    val isVisible: Boolean = false,
    val delta: Double = 1.0,
    val color: Color = Color.DarkGray
)