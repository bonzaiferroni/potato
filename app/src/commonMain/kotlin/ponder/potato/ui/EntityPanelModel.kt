package ponder.potato.ui

import ponder.potato.GameService
import ponder.potato.model.game.Entity
import ponder.potato.model.game.GameState
import pondui.ui.core.StateModel

class EntityPanelModel(
    private val service: GameService = GameService()
): StateModel<EntityPanelState>(EntityPanelState()) {

    val game get() = service.game

    fun setEntityId(entityId: Long) {
        val entity = game.entities[entityId]
        setState { it.copy(
            entity = entity,
            entityName = entity?.name ?: "Entity void",
        ) }
    }
}

data class EntityPanelState(
    val entity: Entity? = null,
    val entityName: String = "",
)