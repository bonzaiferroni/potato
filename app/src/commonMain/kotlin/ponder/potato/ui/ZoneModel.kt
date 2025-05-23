package ponder.potato.ui

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import ponder.potato.GameService
import ponder.potato.model.game.*
import pondui.ui.core.StateModel

class ZoneModel(
    private val zoneId: Int,
    private val service: GameService = GameService()
) : StateModel<ZoneState>(ZoneState(zoneId)) {
    val game get() = service.game
    val zone get() = game.zones.first { it.id == stateNow.zoneId }
    val potato get() = game.potato

    fun init() {
        val bard = game.entities.readEntity<Bard>()
        if (bard != null && bard.zone != zone) {
            bard.travelTo(BOUNDARY / 2, BOUNDARY / 2, zoneId)
        }
    }

    fun update(gameState: GameState) {
        refreshState(gameState.delta)
    }

    fun refreshState(delta: Double = 1.0) {
        setState { it.copy(
            delta = delta,
            entityActions = zone.getEntityActions().toImmutableList(),
            zoneActions = zone.getZoneActions().toImmutableList(),
            statuses = zone.getStatus().toImmutableList(),
            messages = service.messages
        ) }
    }

    fun onHoverChange(entityId: Long, isHovered: Boolean) {
        if (isHovered) {
            setState { it.copy(highlightedId = entityId) }
        } else if (entityId == stateNow.highlightedId) {
            setState { it.copy(highlightedId = null) }
        }
    }

    fun selectEntity(entityId: Long?) {
        setState { it.copy(selectedId = entityId) }
    }
}

data class ZoneState(
    val zoneId: Int = 0,
    val delta: Double = 1.0,
    val entityActions: ImmutableList<EntityAction> = persistentListOf(),
    val zoneActions: ImmutableList<ZoneAction> = persistentListOf(),
    val statuses: ImmutableList<ZoneStatus> = persistentListOf(),
    val messages: List<String> = emptyList(),
    val highlightedId: Long? = null,
    val selectedId: Long? = null,
)

