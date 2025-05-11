package ponder.potato.model.game.components

import ponder.potato.model.game.Inspirit
import ponder.potato.model.game.approach
import ponder.potato.model.game.entities.Intent
import ponder.potato.model.game.entities.StateEntity

class Rester(
    override val entity: StateEntity<TargetState>,
    val findTarget: (StateEntity<TargetState>) -> Boolean
): StateComponent<TargetState>() {

    override fun update(delta: Double) {
        if (entity.hasOtherIntent(Intent.Rest)) return
        if (entity.spiritFull) {
            state.intent = null
            return
        }

        if (entity.isIdle) {
            val target = entity.findTarget(isTarget = findTarget)
            if (target != null) {
                state.intent = Intent.Rest
                entity.target = target
            }
            return
        }

        val target = entity.readOrClearTarget<TargetState>()
        if (target == null) {
            state.intent = null
            return
        }

        val arrived = entity.approach(target.position, delta, 0)
        if (!arrived) return

        val spiritState = entity.state as? SpiritState ?: return

        val spiritGain = minOf(10, spiritState.maxSpirit - spiritState.spirit)
        spiritState.spirit += spiritGain
        println(spiritState.spirit)
        entity.showEffect { Inspirit(spiritGain) }
    }
}