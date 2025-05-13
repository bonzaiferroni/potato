package ponder.potato.model.game

import kabinet.utils.random

class Outcrop(
    override val state: OutcropState,
): StateEntity<OutcropState>() {
    override val components = emptyList<StateComponent<OutcropState>>()
}

data class OutcropState(
    override val isAlive: Boolean,
    override val position: MutablePosition,
    override var log: String?,
    override var intent: Intent?,
    override var progress: Float = 0f,
    override val hardness: Float = 4f,
): MinerTargetState {
    override val composition: Map<Resource, Float> get() = basicComposition
}

interface MinerTargetState: ProgressState {
    val hardness: Float
    val composition: Map<Resource, Float>
}

val basicComposition = mapOf(
    Resource.Dirt to .9f,
    Resource.Gold to .1f,
)

fun <T> Map<T, Float>.roll(): T {
    val roll = Float.random(this.values.sum())
    var sum = 0f
    return this.entries.first {
        sum += it.value
        sum >= roll
    }.key
}