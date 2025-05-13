package ponder.potato.model.game

import kotlinx.serialization.Serializable
import ponder.potato.model.game.entities.EntityState
import ponder.potato.model.game.zones.*

class GameBuilder {
}

fun generateGame(data: GameData, namingWay: NamingWay): GameEngine {
    val cave = Cave()
    val dream = Dream(data.dream)
    val game = GameEngine(data.game, data.resources, namingWay, data.entityStates)
    val village = Village(cave)
    val mine = Mine(cave)
    // val cave = Cave(dream)
    game.add(dream)
    game.add(cave)
    game.add(village)
    game.add(mine)
    return game
}

@Serializable
data class GameData(
    // val caveState: CaveState = CaveState(),
    // val brooms: List<BroomState> = emptyList(),
    // val potato: PotatoState = PotatoState(),
    val dream: DreamState = DreamState(),
    val game: GameState = GameState(),
    val resources: GameStorage = GameStorage(),
    val entityStates: Map<Long, EntityState> = mapOf()
)