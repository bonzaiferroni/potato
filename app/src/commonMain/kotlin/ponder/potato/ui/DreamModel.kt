package ponder.potato.ui

import ponder.potato.GameModel
import ponder.potato.GameService
import ponder.potato.model.game.zones.Dream
import ponder.potato.model.game.zones.DreamState
import ponder.potato.model.game.zones.GameState
import pondui.ui.core.StateModel

class DreamScreenModel(
    private val service: GameService = GameService()
): StateModel<DreamScreenState>(DreamScreenState()) {

    private val game get() = service.game

    fun update(gameState: GameState) {
        setState { it.copy(aether = game.resources.aether) }
    }
}

data class DreamScreenState(
    val aether: Double = 0.0
)