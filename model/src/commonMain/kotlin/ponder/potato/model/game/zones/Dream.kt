package ponder.potato.model.game.zones

import kotlinx.serialization.Serializable
import ponder.potato.model.game.AetherReward
import ponder.potato.model.game.GameResources
import ponder.potato.model.game.components.DreamerState
import ponder.potato.model.game.factorValue

class Dream(
    val state: DreamState,
    val resources: GameResources,
    val cave: Cave,
) : GameZone() {

    val canDive get() = resources.aether > state.levelCost
    val canManifestSprite get() = resources.aether > state.spriteCost

    override fun update(delta: Double) {
        state.spriteCount = cave.sprites
        if (state.progress >= state.resolution) {
            if (resources.aether >= state.aetherMax) return
            var dreamerReward = 0.0
            for (entity in game.entities.values) {
                val dreamer = entity.castIfState<DreamerState>() ?: continue
                dreamerReward += dreamer.state.aetherReward
                dreamer.showEffect { AetherReward(dreamer.state.aetherReward) }
            }

            val totalAether = resources.aether + state.reward + dreamerReward
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

    fun dive() {
        if (!canDive) return
        resources.aether -= state.levelCost
        state.level ++
    }

    fun manifestSprite() {
        if (!canManifestSprite) return
        resources.aether -= state.spriteCost
        cave.manifestSprite()
    }
}

@Serializable
data class DreamState(
    var level: Int = 1,
    var count: Int = 0,
    var progress: Double = 0.0,
    var aetherMax: Double = 1000.0,
    var spriteCount: Int = 0
) {
    val power get() = factorValue(DREAM_PROGRESS_BASE, level, DREAM_PROGRESS_POWER)
    val resolution get() = factorValue(DREAM_GOAL_BASE, level, DREAM_GOAL_POWER)
    val reward get() = factorValue(AETHER_GROWTH_BASE, level, AETHER_GROWTH_POWER)
    val levelCost get() = factorValue(LEVEL_COST_BASE, level, LEVEL_COST_POWER)
    val spriteCost get() = 100.0
}

const val DREAM_PROGRESS_BASE = 30
const val DREAM_PROGRESS_POWER = 1.2
const val DREAM_GOAL_BASE = 100
const val DREAM_GOAL_POWER = 1.4
const val AETHER_GROWTH_BASE = 30
const val AETHER_GROWTH_POWER = 1.0
const val LEVEL_COST_BASE = 100
const val LEVEL_COST_POWER = 1.2