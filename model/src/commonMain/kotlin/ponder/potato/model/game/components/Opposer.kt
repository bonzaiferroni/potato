package ponder.potato.model.game.components

import ponder.potato.model.game.approach
import ponder.potato.model.game.entities.*
import ponder.potato.model.game.moveToward
import ponder.potato.model.game.squaredDistanceTo

interface OpposerState : TargetState {
    val power: Int
}

class Opposer(
    override val entity: StateEntity<OpposerState>,
    val range: Float = Float.MAX_VALUE,
    val isTarget: (StateEntity<SpiritState>) -> Boolean,
) : StateComponent<OpposerState>() {

    override fun update(delta: Double) {
        if (state.intent == null) {
            val target = entity.findTarget(range, isTarget)
            if (target == null) return
            state.intent = Intent.Oppose
        }

        if (state.intent != Intent.Oppose) return

        val target = entity.readOrClearTarget<SpiritState>()

        if (target == null) {
            state.intent = null
            return
        }

        if (target.zone.id != entity.zone.id) {
            state.targetId = null
            state.intent = null
            return
        }

        entity.approach(target.position, delta)
    }
}