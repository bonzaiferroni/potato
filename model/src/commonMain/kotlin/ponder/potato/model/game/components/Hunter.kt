package ponder.potato.model.game.components

import ponder.potato.model.game.entities.*
import ponder.potato.model.game.*
import ponder.potato.model.game.zones.Zone

class Hunter(
    override val entity: StateEntity<OpposerState>,
    val findTarget: (StateEntity<SpiritState>) -> Boolean
) : StateComponent<OpposerState>() {

    private var previousZone: Zone? = null

    override fun update(delta: Double) {
        super.update(delta)

        val target = state.oppositionId?.let { game.entities.read(it) }
            ?: game.entities.findNearest<StateEntity<SpiritState>>(entity.zone, entity.position, findTarget)
                ?.also { state.oppositionId = it.id }

        if (target == null) {
            val portal = entity.zone.portals.firstOrNull { it.zone != previousZone }
            if (portal != null) {
                val distance = entity.position.squaredDistanceTo(portal)
                if (distance > 0) {
                    entity.moveToward(portal, delta)
                } else {
                    previousZone = entity.zone
                    portal.transport(entity)
                }
            }
            return
        }

        val squaredDistance = entity.position.squaredDistanceTo(target.position)
        if (squaredDistance > 1) {
            entity.moveToward(target.position, delta)
        }
    }
}