package ponder.potato

import ponder.potato.model.game.GameData
import ponder.potato.model.game.generateGame
import ponder.potato.model.game.zones.Game

class GameService {
    val game get() = _game

    companion object {
        private val _game: Game = generateGame(GameData())
    }
}