package ponder.potato.ui

import ponder.potato.GameService
import pondui.ui.core.StateModel

class EntityPanelModel(
    entityId: Long,
    private val service: GameService = GameService()
): StateModel<EntityPanelState>(EntityPanelState(entityId)) {

    val game get() = service.game

    init {
        refreshEntity()
    }

    fun refreshEntity(entityId: Long = stateNow.entityId) {
        val entity = game.entities[entityId] ?: return
        setState { it.copy(
            entityId = entityId,
            entityName = entity.name
        ) }
    }
}

data class EntityPanelState(
    val entityId: Long,
    val entityName: String = "",
)