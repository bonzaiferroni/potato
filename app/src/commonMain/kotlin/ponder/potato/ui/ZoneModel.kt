package ponder.potato.ui

import ponder.potato.GameService
import ponder.potato.model.game.Resource
import ponder.potato.model.game.zones.GameState
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
        val resource = game.resources.readQuantity(Resource.Aether)
    }
}

data class ZoneState(
    val zoneId: Int = 0,
    val resource: Double = 0.0,
    val resourceMax: Double = 0.0,
    val dreamLevel: Int = 0,
    val delta: Double = 1.0,
)

