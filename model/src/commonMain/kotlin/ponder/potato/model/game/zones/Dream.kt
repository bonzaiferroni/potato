package ponder.potato.model.game.zones

import kotlinx.serialization.Serializable
import ponder.potato.model.game.AetherReward
import ponder.potato.model.game.BOUNDARY_X
import ponder.potato.model.game.BOUNDARY_Y
import ponder.potato.model.game.Resource
import ponder.potato.model.game.components.DreamerState
import ponder.potato.model.game.components.readResourceMax
import ponder.potato.model.game.count
import ponder.potato.model.game.entities.Bard
import ponder.potato.model.game.factorValue

class Dream(
    val state: DreamState,
) : GameZone() {

    val level get() = game.dreamLevel
    val levelCost get() = factorValue(1200, level, 1.2)
    val canDive get() = resources.aether > levelCost

    fun dreamBard() {
        val cave = game.zones.readOrNull<Cave>() ?: error("no cave zone")
        game.spawn(cave, BOUNDARY_X / 2, BOUNDARY_Y / 2) { Bard() }
    }

    override fun getActions(): List<ZoneAction> = listOf(
        ZoneAction(
            action = Actions.ResolveDream,
            status = null,
            cost = levelCost,
            currentResource = game.resources.aether,
            count = level,
            maxCount = null,
            block = ::resolve,
        ),
        ZoneAction(
            action = Actions.DreamBard,
            status = null,
            cost = 500.0,
            currentResource = game.resources.aether,
            count = game.entities.count<Bard>(),
            maxCount = 1,
            block = ::dreamBard,
        )
    )

    override fun getStatus() = listOf(
        IntValue("Dream level", game.dreamLevel),
        ProgressValue("Dreaming...", (state.progress / state.resolution).toFloat()),
        ResourceStatus(game.resources.aether, state.aetherMax, Resource.Aether)
    )

    override fun update(delta: Double) {
        state.aetherMax = game.entities.readResourceMax(Resource.Aether)

        if (state.progress >= state.resolution) {
            if (resources.aether >= state.aetherMax) return
            var dreamerReward = 0.0
            for (entity in game.entities.values) {
                val dreamer = entity.castIfState<DreamerState>() ?: continue
                dreamerReward += dreamer.state.getReward(level)
                dreamer.showEffect { AetherReward(dreamer.state.aetherReward) }
            }

            val totalAether = resources.aether + dreamerReward
            resources.aether = minOf(totalAether, state.aetherMax)
            state.progress = 0.0
        }

        val totalProgress = state.progress + state.power * delta
        if (totalProgress > state.resolution) {
            state.progress = state.resolution
        } else {
            state.progress = totalProgress
        }
    }

    fun resolve() {
        if (!canDive) return
        resources.aether -= levelCost
        game.potato?.state?.level += 1
    }
}

@Serializable
data class DreamState(
    var count: Int = 0,
    var progress: Double = 0.0,
    var aetherMax: Double = 0.0,
) {
    val power get() = 25.0// factorValue(30, level, 1.2)
    val resolution get() = 100.0 // factorValue(100, level, 1.4)
}