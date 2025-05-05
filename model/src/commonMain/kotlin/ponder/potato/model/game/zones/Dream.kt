package ponder.potato.model.game.zones

import kotlinx.serialization.Serializable
import ponder.potato.model.game.AetherReward
import ponder.potato.model.game.components.DreamerState
import ponder.potato.model.game.components.readAetherMax
import ponder.potato.model.game.factorValue

class Dream(
    val state: DreamState,
) : GameZone() {

    val canDive get() = resources.aether > state.levelCost

    override fun update(delta: Double) {
        val aetherMax = game.entities.readAetherMax()
        state.level = game.potato?.level ?: 1

        if (state.progress >= state.resolution) {
            if (resources.aether >= aetherMax) return
            var dreamerReward = 0.0
            for (entity in game.entities.values) {
                val dreamer = entity.castIfState<DreamerState>() ?: continue
                dreamerReward += dreamer.state.aetherReward
                dreamer.showEffect { AetherReward(dreamer.state.aetherReward) }
            }

            val totalAether = resources.aether + dreamerReward
            resources.aether = minOf(totalAether, aetherMax)
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
        resources.aether -= state.levelCost
        game.potato?.level += 1
    }
}

@Serializable
data class DreamState(
    var level: Int = 1,
    var count: Int = 0,
    var progress: Double = 0.0,
) {
    val power get() = factorValue(30, level, 1.2)
    val resolution get() = factorValue(100, level, 1.4)
    val levelCost get() = factorValue(1200, level, 1.2)
}