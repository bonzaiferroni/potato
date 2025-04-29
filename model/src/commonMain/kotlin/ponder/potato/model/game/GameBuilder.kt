package ponder.potato.model.game

import ponder.potato.model.game.zones.Cave
import ponder.potato.model.game.zones.Dream
import ponder.potato.model.game.zones.DreamState
import ponder.potato.model.game.zones.GameEngine
import ponder.potato.model.game.zones.GameMap
import ponder.potato.model.game.zones.GameState

class GameBuilder {
}

fun generateGame(data: GameData): GameEngine {
    val cave = Cave()
    val dream = Dream(data.dream, data.resources, cave)
    val map = GameMap(dream)
    val game = GameEngine(data.game, data.resources, map)
    // val cave = Cave(dream)
    game.add(dream)
    game.add(cave)
    return game
}

data class GameData(
    // val caveState: CaveState = CaveState(),
    // val brooms: List<BroomState> = emptyList(),
    // val potato: PotatoState = PotatoState(),
    val dream: DreamState = DreamState(),
    val game: GameState = GameState(),
    val resources: GameResources = GameResources()
)