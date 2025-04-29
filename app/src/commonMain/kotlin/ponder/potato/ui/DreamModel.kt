package ponder.potato.ui

import ponder.potato.GameModel
import ponder.potato.GameService
import ponder.potato.model.game.zones.Dream
import ponder.potato.model.game.zones.DreamState
import ponder.potato.model.game.zones.GameState
import pondui.ui.core.StateModel

class DreamScreenModel(
    private val service: GameService = GameService()
) : StateModel<DreamScreenState>(DreamScreenState()) {

    private val game get() = service.game

    fun update(gameState: GameState) {
        val dream = game.map.dream.state
        val resources = game.resources
        setState {
            it.copy(
                aether = resources.aether,
                tick = gameState.tick,
                progress = dream.progress,
                progressRatio = (dream.progress / dream.progressGoal).toFloat()
            )
        }
    }
}

data class DreamScreenState(
    val tick: Long = 0,
    val aether: Double = 0.0,
    val progress: Double = 0.0,
    val progressRatio: Float = 0f,
)