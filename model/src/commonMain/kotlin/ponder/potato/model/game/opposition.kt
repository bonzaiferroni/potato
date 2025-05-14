package ponder.potato.model.game

import kotlin.random.Random

fun StateEntity<OpposerState>.oppose(target: StateEntity<SpiritState>) {
    val distance = this.position.squaredWorldDistanceTo(target.position)
    if (distance > 1) return
    val damage = (this.state.power * Random.nextFloat()).toInt()

    target.state.spirit -= damage
    if (this.isObserved) this.showEffect(Opposition(target, damage))
    // target.showEffect { Despirit(damage) }
}