package ponder.potato.model.game

import kotlinx.serialization.Serializable

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

    override fun getZoneActions(): List<ZoneAction> = listOf(
        ZoneAction(
            ability = ZoneAbility.ResolveDream,
            status = null,
            cost = levelCost,
            currentResource = game.storage.aether,
            count = level,
            maxCount = null,
            block = ::resolve,
        ),
        ZoneAction(
            ability = ZoneAbility.DreamBard,
            status = null,
            cost = 500.0,
            currentResource = game.storage.aether,
            count = game.entities.count<Bard>(),
            maxCount = 1,
            block = ::dreamBard,
        )
    )

    override fun getStatus() = listOf(
        IntValue("Dream level", game.dreamLevel),
        ProgressValue("Dreaming...", (state.progress / state.resolution).toFloat()),
        ResourceStatus(game.storage.aether, state.aetherMax, Resource.Aether)
    )

    override fun update(delta: Double) {
        state.aetherMax = game.entities.readResourceMax(Resource.Aether)

        if (state.progress >= state.resolution) {
            if (resources.aether >= state.aetherMax) return
            var dreamerReward = 0.0
            for (entity in game.entities.values) {
                val dreamer = entity.castIfState<DreamerState>() ?: continue
                dreamerReward += dreamer.state.getReward(level)
                if (dreamer.isObserved) dreamer.showEffect(ResourceReward(dreamer.state.aetherReward))
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