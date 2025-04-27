package ponder.potato.model.game

import kotlinx.serialization.Serializable

class Dream(
    initialState: DreamState
) : StateEngine<DreamState>(initialState) {
    override fun update(delta: Double) {
        val totalProgress = stateNow.progress + stateNow.progressUnit * delta
        if (totalProgress > stateNow.progressGoal) {
            setState { it.copy(
                progress = 0.0,
                count = stateNow.count + 1,
                aether = stateNow.aether + stateNow.aetherGrowth
            ) }
        } else {
            setState { it.copy(progress = totalProgress) }
        }
    }
}

@Serializable
data class DreamState(
    val level: Int = 1,
    val count: Int = 0,
    val progress: Double = 0.0,
    val aether: Double = 0.0,
) {
    val progressUnit get() = factorValue(DREAM_PROGRESS_BASE, level, DREAM_PROGRESS_POWER)
    val progressGoal get() = factorValue(DREAM_GOAL_BASE, level, DREAM_GOAL_POWER)
    val aetherGrowth get() = factorValue(AETHER_GROWTH_BASE, level, AETHER_GROWTH_POWER)
}

const val DREAM_PROGRESS_BASE = 100.0
const val DREAM_PROGRESS_POWER = 1.2
const val DREAM_GOAL_BASE = 10000.0
const val DREAM_GOAL_POWER = 1.4
const val AETHER_GROWTH_BASE = 10.0
const val AETHER_GROWTH_POWER = 1.0