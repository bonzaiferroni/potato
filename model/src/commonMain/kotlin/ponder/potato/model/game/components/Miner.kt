package ponder.potato.model.game.components

import ponder.potato.model.game.approach
import ponder.potato.model.game.entities.MinerTargetState
import ponder.potato.model.game.entities.Intent
import ponder.potato.model.game.entities.StateEntity
import ponder.potato.model.game.entities.roll

class Miner(
    override val entity: StateEntity<TargetState>
): StateComponent<TargetState>() {

    override fun update(delta: Double) {
        if (!entity.hasIntent(Intent.Mine)) return
        val target = entity.readTarget<MinerTargetState>() ?: return
        val isArrived = entity.approach(target.position, delta)
        if (!isArrived) return

        if (target.state.progress < 1f) {
            val additionalProgress = (delta / target.state.hardness).toFloat() // todo: add power
            target.state.progress += minOf(additionalProgress, 1f - target.state.progress)
            return
        }

        val quantity = 10.0 // todo: factor quantity
        val type = target.state.composition.roll()
        game.storage.addQuantity(type, quantity)
        target.state.progress = 0f
    }
}