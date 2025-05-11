package ponder.potato.model.game

import ponder.potato.model.game.components.OpposerState
import ponder.potato.model.game.components.SpiritState
import ponder.potato.model.game.entities.StateEntity
import kotlin.random.Random

fun StateEntity<OpposerState>.oppose(target: StateEntity<SpiritState>) {
    val distance = this.position.squaredWorldDistanceTo(target.position)
    if (distance > 1) return
    val damage = (this.state.power * Random.nextFloat()).toInt()

    target.state.spirit -= damage
    this.showEffect { Opposition(target, damage) }
    // target.showEffect { Despirit(damage) }
}