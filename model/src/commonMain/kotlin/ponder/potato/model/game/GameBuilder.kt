package ponder.potato.model.game

import kotlinx.serialization.Serializable

class GameBuilder {
}

fun generateGame(data: GameData, namingWay: NamingWay): GameEngine {
    val cave = Cave()
    val dream = Dream(data.dream)
    val game = GameEngine(data.game, data.resources, namingWay, data.entityStates)
    val village = Village(cave)
    val mine = Mine(cave)
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