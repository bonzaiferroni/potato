package ponder.potato.model.game.components

import ponder.potato.model.game.entities.*
import ponder.potato.model.game.*

class Hunter(
    override val entity: StateEntity<OpposerState>,
    val findTarget: () -> StateEntity<SpiritState>?,
): StateComponent<OpposerState>() {

    override fun update(delta: Double) {
        super.update(delta)
        val target = state.oppositionId?.let { game.entities.read(it) }
            ?: findTarget()?.also { state.oppositionId = it.id }
            ?: return

        val squaredDistance = entity.position.squaredDistanceTo(target.position)
        if (squaredDistance > 1) {
            entity.moveToward(target.position, delta)
        } else {
            entity.oppose(target)
        }
    }
}