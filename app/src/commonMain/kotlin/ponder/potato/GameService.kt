package ponder.potato

import ponder.potato.model.game.GameData
import ponder.potato.model.game.generateGame
import ponder.potato.model.game.zones.Game
import ponder.potato.model.game.zones.GameEngine

class GameService {
    val game: Game get() = engine

    fun update(delta: Double) {
        engine.update(delta)
    }

    companion object {
        private val engine: GameEngine = generateGame(GameData())
    }
}