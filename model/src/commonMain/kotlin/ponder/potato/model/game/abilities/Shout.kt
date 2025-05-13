package ponder.potato.model.game.abilities

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ponder.potato.model.game.components.despirit
import ponder.potato.model.game.components.target
import ponder.potato.model.game.entities.EntityState
import ponder.potato.model.game.entities.StateEntity
import ponder.potato.model.game.zones.EntityAbility
import ponder.potato.model.game.zones.EntityAction

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