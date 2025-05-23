package ponder.potato.ui

import ponder.potato.model.game.NameState
import ponder.potato.model.game.SpiritState
import ponder.potato.model.game.Entity
import ponder.potato.model.game.Game
import ponder.potato.model.game.GameState

class EntityListModel(
    val zoneId: Int? = null,
): GameStateModel<EntityListState>(EntityListState()) {

    fun update(gameState: GameState) {
        val entities = game.entities.values.filter { zoneId == null || zoneId == it.position.zoneId }
            .map { it.toEntityInfo(game) }
        setState { it.copy(entities = entities) }
    }
}

data class EntityListState(
    val entities: List<EntityInfo> = emptyList()
)

data class EntityInfo(
    val id: Long,
    val type: String,
    val x: Float,
    val y: Float,
    val zoneId: Int,
    val zoneName: String,
    val spirit: Int?,
    val maxSpirit: Int?,
    val name: String?,
)

fun Entity.toEntityInfo(game: Game) = EntityInfo(
    id = id,
    type = this::class.simpleName ?: "Unknown",
    x = state.position.x,
    y = state.position.y,
    zoneId = state.position.zoneId,
    zoneName = game.getZone(state.position.zoneId).name,
    spirit = (state as? SpiritState)?.spirit,
    maxSpirit = (state as? SpiritState)?.maxSpirit,
    name = (state as? NameState)?.name,
)