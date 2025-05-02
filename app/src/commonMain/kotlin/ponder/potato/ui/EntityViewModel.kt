package ponder.potato.ui

import androidx.compose.ui.graphics.Color
import ponder.potato.GameService
import ponder.potato.model.game.components.SpiritState
import ponder.potato.model.game.entities.Sprite
import ponder.potato.model.game.entities.StateEntity
import ponder.potato.model.game.zones.GameState
import pondui.ui.core.StateModel

class EntityViewModel(
    val entityId: Long,
    val gameService: GameService = GameService()
): StateModel<EntityViewState>(EntityViewState()) {
    val game = gameService.game
    val entity get() = game.entities[entityId]
    val type = entity?.let { it::class.simpleName } ?: "Unknown"

    fun update(gameState: GameState) {
        entity?.let { e ->
            val spiritState = e.state as? SpiritState
            setState { it.copy(
                x = e.position.x,
                y = e.position.y,
                isVisible = true,
                delta = gameState.delta,
                isTeleported = !stateNow.isVisible,
                spirit = spiritState?.spirit,
                spiritMax = spiritState?.maxSpirit
            ) }
        }
    }
}

data class EntityViewState(
    val x: Float = 0f,
    val y: Float = 0f,
    val isVisible: Boolean = false,
    val delta: Double = 1.0,
    val isTeleported: Boolean = false,
    val spirit: Int? = null,
    val spiritMax: Int? = null,
) {
    val spiritRatio = if (spiritMax == null || spirit == null || spiritMax == 0) 0f else spirit / spiritMax.toFloat()
}