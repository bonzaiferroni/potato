package ponder.potato.model.game

import kabinet.utils.random
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class Outcrop(
    override val state: OutcropState = OutcropState(),
): StateEntity<OutcropState>() {
    override val components = emptyList<StateComponent<OutcropState>>()
}

@Serializable
data class OutcropState(
    override val position: MutablePosition = MutablePosition(),
    override var intent: Intent? = null,
    override val hardness: Float = 4f,
): MinerTargetState, WorkTarget {
    override val isAlive: Boolean = true
    override val composition: Map<Resource, Float> get() = basicComposition
}

interface MinerTargetState: EntityState {
    val hardness: Float
    val composition: Map<Resource, Float>
}

interface WorkTarget: EntityState

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