package ponder.potato.ui

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import ponder.potato.GameService
import ponder.potato.model.game.zones.EntityAction
import ponder.potato.model.game.zones.GameState
import ponder.potato.model.game.zones.ZoneAction
import ponder.potato.model.game.zones.ZoneStatus
import pondui.ui.core.StateModel

class ZoneModel(
    private val zoneId: Int,
    private val service: GameService = GameService()
) : StateModel<ZoneState>(ZoneState(zoneId)) {
    val game get() = service.game
    val zone get() = game.zones.first { it.id == stateNow.zoneId }
    val potato get() = game.potato

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

