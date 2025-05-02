package ponder.potato.ui

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ponder.potato.GameService
import ponder.potato.model.game.Effect
import ponder.potato.model.game.components.SpiritState
import ponder.potato.model.game.entities.Entity
import ponder.potato.model.game.entities.Sprite
import ponder.potato.model.game.entities.StateEntity
import ponder.potato.model.game.zones.GameState
import pondui.ui.core.StateModel

class EntityViewModel(
    val entityId: Long,
    val gameService: GameService = GameService()
): StateModel<EntityViewState>(EntityViewState()) {
    val game = gameService.game
    val entity = game.entities[entityId] as? StateEntity<*>
    val type = entity?.let { it::class.simpleName } ?: "Unknown"

    init {
        viewModelScope.launch {
            entity?.effects?.collect { effect ->
                setState {
                    it.copy(
                        effects = stateNow.effects + ObservedEffect(effect, Clock.System.now())
                    )
                }
            }
        }
    }

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
    val effects: List<ObservedEffect> = emptyList()
) {
    val spiritRatio = if (spiritMax == null || spirit == null || spiritMax == 0) 1f else spirit / spiritMax.toFloat()
}

data class ObservedEffect(
    val effect: Effect,
    val time: Instant,
)

const val EFFECT_DISPLAY_SECONDS = 3