package ponder.potato.ui

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import ponder.potato.GameService
import ponder.potato.model.game.BOUNDARY_X
import ponder.potato.model.game.BOUNDARY_Y
import ponder.potato.model.game.Bard
import ponder.potato.model.game.read
import ponder.potato.model.game.EntityAction
import ponder.potato.model.game.GameState
import ponder.potato.model.game.ZoneAction
import ponder.potato.model.game.ZoneStatus
import ponder.potato.model.game.travelTo
import pondui.ui.core.StateModel

class ZoneModel(
    private val zoneId: Int,
    private val service: GameService = GameService()
) : StateModel<ZoneState>(ZoneState(zoneId)) {
    val game get() = service.game
    val zone get() = game.zones.first { it.id == stateNow.zoneId }
    val potato get() = game.potato

    fun init() {
        val bard = game.entities.read<Bard>()
        if (bard != null && bard.zone != zone) {
            bard.travelTo(BOUNDARY_X / 2, BOUNDARY_Y / 2, zoneId)
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
        ) }
    }
}

data class ZoneState(
    val zoneId: Int = 0,
    val delta: Double = 1.0,
    val entityActions: ImmutableList<EntityAction> = persistentListOf(),
    val zoneActions: ImmutableList<ZoneAction> = persistentListOf(),
    val statuses: ImmutableList<ZoneStatus> = persistentListOf()
)

