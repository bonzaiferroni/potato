package ponder.potato.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ponder.potato.GameService
import ponder.potato.model.game.Effect
import ponder.potato.model.game.SpiritState
import ponder.potato.model.game.StateEntity
import ponder.potato.model.game.GameState
import ponder.potato.model.game.ProgressState
import pondui.ui.core.StateModel
import kotlin.time.Duration.Companion.seconds

class EntityViewModel(
    val entityId: Long,
    val gameService: GameService = GameService()
): StateModel<EntityViewState>(EntityViewState()) {
    val game = gameService.game
    val entity = game.entities[entityId] as? StateEntity<*>
    val type = entity?.let { it::class.simpleName } ?: "Unknown"
    private val _effects = mutableStateListOf<ObservedEffect>()
    val effects: SnapshotStateList<ObservedEffect> = _effects

    init {
        viewModelScope.launch {
            entity?.effects?.collect { effect ->
                val now = Clock.System.now()
                _effects.add(ObservedEffect(effect, now, "$${effect::class.simpleName}-${now.toEpochMilliseconds()}"))
            }
        }
        refreshState()
    }

    fun update(gameState: GameState) {
        if (effects.isNotEmpty() && effects.all { (Clock.System.now() - it.time) > (EFFECT_DISPLAY_SECONDS + 2).seconds }) {
            effects.clear()
        }
        refreshState(gameState.delta)
    }

    fun refreshState(delta: Double = 0.0) {
        entity?.let { e ->
            val spiritState = e.state as? SpiritState
            setState { it.copy(
                x = e.position.x,
                y = e.position.y,
                isVisible = delta > 0,
                delta = delta,
                spirit = spiritState?.spirit,
                spiritMax = spiritState?.maxSpirit,
                isMoving = e.position.x != stateNow.x || e.position.y != stateNow.y,
                progress = (e.state as? ProgressState)?.progress
            ) }
        }
    }

    fun dispose() {
        setState {
            it.copy(isVisible = false)
        }
    }
}

data class EntityViewState(
    val x: Float = 0f,
    val y: Float = 0f,
    val scale: Float = 1f,
    val isVisible: Boolean = false,
    val delta: Double = 1.0,
    val spirit: Int? = null,
    val spiritMax: Int? = null,
    val isMoving: Boolean = false,
    val progress: Float? = null,
) {
    val spiritRatio = if (spiritMax == null || spirit == null || spiritMax == 0) 1f else spirit / spiritMax.toFloat()
}

data class ObservedEffect(
    val effect: Effect,
    val time: Instant,
    val key: String,
)

const val EFFECT_DISPLAY_SECONDS = 3