package ponder.potato.ui

import ponder.potato.GameService
import ponder.potato.model.game.zones.GameState
import pondui.ui.core.StateModel

class DreamScreenModel(
    private val service: GameService = GameService()
) : StateModel<DreamScreenState>(DreamScreenState()) {

    private val game get() = service.game

    fun update(gameState: GameState) {
        val dream = game.map.dream
        val resources = game.resources
        setState {
            it.copy(
                aether = resources.aether,
                tick = gameState.tick,
                progress = dream.state.progress,
                progressRatio = (dream.state.progress / dream.state.progressGoal).toFloat(),
                levelRatio = minOf(1.0, resources.aether / dream.state.levelCost).toFloat()
            )
        }
    }
}

data class DreamScreenState(
    val tick: Long = 0,
    val aether: Double = 0.0,
    val progress: Double = 0.0,
    val progressRatio: Float = 0f,
    val levelRatio: Float = 0f,
)