package ponder.potato.model.game

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.minutes

class Hunter(
    override val entity: StateEntity<OpposerState>,
    val findTarget: (StateEntity<SpiritState>) -> Boolean
) : StateComponent<OpposerState>() {

    private var previousZone: Zone? = null
    private var zonedAt: Instant = Instant.DISTANT_PAST

    override fun update(delta: Double) {
        if (entity.hasOtherIntent(Intent.Oppose)) return

        val target = entity.readOrFindTarget(isTarget = findTarget)

        if (target == null) {
            val portal = entity.zone.portals.firstOrNull { it.destination != previousZone }
                ?: if ((Clock.System.now() - zonedAt) > 1.minutes) entity.zone.portals.firstOrNull() else null
            if (portal != null) {
                val distance = entity.position.squaredDistanceTo(portal)
                if (distance > 0) {
                    entity.moveToward(portal, delta)
                } else {
                    previousZone = entity.zone
                    zonedAt = Clock.System.now()
                    portal.transport(entity)
                }
            }
            return
        }

        state.intent = Intent.Oppose
        val squaredDistance = entity.position.squaredDistanceTo(target.position)
        if (squaredDistance > 1) {
            entity.moveToward(target.position, delta)
        }
    }
}