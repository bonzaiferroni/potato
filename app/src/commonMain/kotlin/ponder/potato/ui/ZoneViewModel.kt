@file:Suppress("UNCHECKED_CAST")

package ponder.potato.ui

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import ponder.potato.GameService
import ponder.potato.model.game.Bard
import ponder.potato.model.game.readEntity
import ponder.potato.model.game.GameState
import ponder.potato.model.game.Zone
import pondui.ui.core.StateModel
import kotlin.reflect.KClass

class ZoneViewModel<T : Zone>(
    zoneClass: KClass<T>,
    gameService: GameService = GameService()
) : StateModel<ZoneViewState>(ZoneViewState(zoneClass.simpleName ?: "not found")) {
    private val game = gameService.game

    val zone = game.zones.find { zoneClass.isInstance(it) } ?: error("Zone not found")

    fun update(gameState: GameState) {
        val entityIds = game.entities.values.filter { it.position.zoneId == zone.id }.map{ it.id }.toImmutableList()
        val bardPresent = game.entities.readEntity<Bard>()?.position?.zoneId == zone.id
        val exits = zone.portals.map { ZoneExit(it.destination.id, it.x, it.y) }
        setState { it.copy(entityIds = entityIds, fullVisibility = true, exits = exits) }
    }
}

data class ZoneViewState(
    val name: String = "",
    val entityIds: ImmutableList<Long> = persistentListOf(),
    val fullVisibility: Boolean = false,
    val exits: List<ZoneExit> = emptyList()
)

data class ZoneExit(
    val zoneId: Int,
    val x: Float,
    val y: Float,
)