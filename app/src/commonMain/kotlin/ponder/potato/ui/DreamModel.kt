package ponder.potato.ui

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import ponder.potato.GameService
import ponder.potato.model.game.Resource
import ponder.potato.model.game.components.readResourceMax
import ponder.potato.model.game.entities.Potato
import ponder.potato.model.game.read
import ponder.potato.model.game.zones.Cave
import ponder.potato.model.game.zones.Dream
import ponder.potato.model.game.zones.GameState
import ponder.potato.model.game.zones.ZoneAction
import ponder.potato.model.game.zones.ZoneStatus
import ponder.potato.model.game.zones.readZone
import pondui.ui.core.StateModel

class DreamModel(
    private val service: GameService = GameService()
) : StateModel<DreamState>(DreamState()) {

    private val game get() = service.game
    private val dream = game.readZone<Dream>()
    private val cave = game.readZone<Cave>()
    private val potato get() = game.entities.read<Potato>()
    val caveId = cave.id

    fun update(gameState: GameState) {
        refreshState(gameState.delta)
    }

    fun refreshState(delta: Double = 1.0) {
        val resources = game.resources
        val aetherMax = game.entities.readResourceMax(Resource.Aether)
        val dreamLevel = potato?.state?.level ?: 1
        setState {
            it.copy(
                aether = resources.aether,
                aetherMax = aetherMax,
                level = dreamLevel,
                dreamProgress = dream.state.progress,
                dreamProgressMax = dream.state.resolution,
                levelCost = dream.levelCost,
                delta = delta,
                actions = dream.getZoneActions().toImmutableList(),
                statuses = dream.getStatus().toImmutableList()
            )
        }
    }
}

data class DreamState(
    val aether: Double = 0.0,
    val aetherMax: Double = 0.0,
    val level: Int = 0,
    val levelCost: Double = 0.0,
    val dreamProgress: Double = 0.0,
    val dreamProgressMax: Double = 0.0,
    val delta: Double = 1.0,
    val actions: ImmutableList<ZoneAction> = persistentListOf(),
    val statuses: ImmutableList<ZoneStatus> = persistentListOf()
) {
    val progressRatio get() = dreamProgressMax.takeIf { it > 0 }?.let {
        minOf(1.0, dreamProgress / dreamProgressMax).toFloat().takeIf { it != Float.NaN } ?: 0f
    } ?: 0f
    val aetherRatio get() = aetherMax.takeIf { it > 0 }?.let {
        minOf(1.0, aether / aetherMax).toFloat()
    } ?: 0f
    val levelProgress get() = minOf(1.0, aether / levelCost).toFloat()
}
