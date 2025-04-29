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
                dreamProgress = dream.state.progress,
                dreamProgressMax = dream.state.progressMax,
                levelCost = dream.state.levelCost
            )
        }
    }
}

data class DreamScreenState(
    val aether: Double = 0.0,
    val dreamProgress: Double = 0.0,
    val dreamProgressMax: Double = 0.0,
    val levelCost: Double = 0.0,
) {
    val progressRatio get() = minOf(1.0, dreamProgress / dreamProgressMax).toFloat()
    val levelProgress get() = minOf(1.0, aether / levelCost).toFloat()
}