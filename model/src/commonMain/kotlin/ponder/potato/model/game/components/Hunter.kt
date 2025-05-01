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

        if (entity.position.zoneId != target.position.zoneId) {
            val portal = entity.zone.portals.find { it.zone.id == target.zone.id } ?: return
            val distance = entity.position.squaredDistanceTo(portal)
            if (distance > 0) {
                entity.moveToward(portal, delta)
            } else {
                entity.enter(portal.zone)
            }
            return
        }

        val squaredDistance = entity.position.squaredDistanceTo(target.position)
        if (squaredDistance > 1) {
            entity.moveToward(target.position, delta)
        }
    }
}