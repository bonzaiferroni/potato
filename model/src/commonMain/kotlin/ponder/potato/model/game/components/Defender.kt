package ponder.potato.model.game.components

import ponder.potato.model.game.entities.Entity
import ponder.potato.model.game.entities.StateEntity
import ponder.potato.model.game.read
import ponder.potato.model.game.squaredDistanceTo

class Defender(
    override val entity: StateEntity<OpposerState>
): StateComponent<OpposerState>() {

    override fun update(delta: Double) {
        super.update(delta)

        val target: Entity = state.oppositionId?.let { game.entities.read(it) } ?: return
        val squaredDistance = entity.position.squaredDistanceTo(target.position)
        if (squaredDistance > 1) {

        }
    }
}