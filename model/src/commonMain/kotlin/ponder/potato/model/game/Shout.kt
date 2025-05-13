package ponder.potato.model.game

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

interface ShoutState: EntityState {
    var shoutedAt: Instant?
    val power: Int
}

fun StateEntity<ShoutState>.shoutAtTarget(): EntityAction? {
    val target = this.target ?: return null
    return EntityAction(id, EntityAbility.Shout, lastUsed = state.shoutedAt) {
        state.shoutedAt = Clock.System.now()
        target.despirit(10)
    }
}