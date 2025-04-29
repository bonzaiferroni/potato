package ponder.potato.model.game.zones

import kotlinx.serialization.Serializable
import ponder.potato.model.game.GameResources
import ponder.potato.model.game.factorValue

class Dream(
    val state: DreamState,
    val resources: GameResources
) : GameZone() {
    override fun update(delta: Double) {
        val totalProgress = state.progress + state.progressUnit * delta
        if (totalProgress > state.progressGoal) {
            state.progress = 0.0
            state.count++
            resources.aether += state.aetherGrowth
        } else {
            state.progress = totalProgress
        }
    }
}

@Serializable
data class DreamState(
    var level: Int = 1,
    var count: Int = 0,
    var progress: Double = 0.0,
) {
    val progressUnit get() = factorValue(DREAM_PROGRESS_BASE, level, DREAM_PROGRESS_POWER)
    val progressGoal get() = factorValue(DREAM_GOAL_BASE, level, DREAM_GOAL_POWER)
    val aetherGrowth get() = factorValue(AETHER_GROWTH_BASE, level, AETHER_GROWTH_POWER)
}

const val DREAM_PROGRESS_BASE = 100
const val DREAM_PROGRESS_POWER = 1.2
const val DREAM_GOAL_BASE = 10000
const val DREAM_GOAL_POWER = 1.4
const val AETHER_GROWTH_BASE = 10
const val AETHER_GROWTH_POWER = 1.0