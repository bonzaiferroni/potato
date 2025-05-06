package ponder.potato.model.game

import kotlinx.serialization.Serializable
import ponder.potato.model.game.entities.EntityState
import ponder.potato.model.game.zones.Cave
import ponder.potato.model.game.zones.Dream
import ponder.potato.model.game.zones.DreamState
import ponder.potato.model.game.zones.GameEngine
import ponder.potato.model.game.zones.GameMap
import ponder.potato.model.game.zones.GameState
import ponder.potato.model.game.zones.Village

class GameBuilder {
}

fun generateGame(data: GameData): GameEngine {
    val cave = Cave()
    val dream = Dream(data.dream)
    val game = GameEngine(data.game, data.resources, data.entityStates)
    val village = Village(cave)
    // val cave = Cave(dream)
    game.add(dream)
    game.add(cave)
    game.add(village)
    return game
}

@Serializable
data class GameData(
    // val caveState: CaveState = CaveState(),
    // val brooms: List<BroomState> = emptyList(),
    // val potato: PotatoState = PotatoState(),
    val dream: DreamState = DreamState(),
    val game: GameState = GameState(),
    val resources: GameResources = GameResources(),
    val entityStates: Map<Long, EntityState> = mapOf()
)