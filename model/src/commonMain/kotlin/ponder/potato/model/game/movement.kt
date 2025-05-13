package ponder.potato.model.game

import ponder.potato.model.game.components.MoverState
import ponder.potato.model.game.entities.Entity
import ponder.potato.model.game.entities.Intent
import ponder.potato.model.game.entities.StateEntity
import ponder.potato.model.game.zones.GameZone
import ponder.potato.model.game.zones.Portal
import kotlin.math.sqrt
import kotlin.random.Random

fun Entity.moveToward(position: Position, delta: Double) {
    val potential = delta * state.speed
    val dx = position.x - state.position.x
    val dy = position.y - state.position.y
    val distance = sqrt(dx * dx + dy * dy)
    if (distance <= potential || distance == 0f) {
        state.position.x = position.x
        state.position.y = position.y
        return
    }

    val ratio = potential / distance
    state.position.x += (dx * ratio).toFloat()
    state.position.y += (dy * ratio).toFloat()
}

fun StateEntity<*>.approach(position: Position, delta: Double, range: Int = 1): Boolean {
    if (this.zone.id != position.zoneId) return false
    val squaredDistance = this.position.squaredDistanceTo(position)
    val squaredRange = range * range
    if (squaredDistance > squaredRange) {
        this.moveToward(position, delta)
        return false
    }

    return true
}

fun StateEntity<*>.moveTo(position: Position, delta: Double, range: Int = 1): Boolean {
    if (position.zoneId != this.zone.id) {
        val portal = zone.findRoutePortal(position)
        if (portal == null) return false

        val isArrived = approach(portal, delta, 0)
        if (!isArrived) return false

        portal.transport(this)
        return false
    }

    return approach(position, delta, range)
}

fun GameZone.findRoutePortal(position: Position, originId: Int? = null): Portal? {
    for (portal in portals) {
        if (portal.destination.id == position.zoneId) return portal
    }
    for (portal in portals) {
        if (originId != null && portal.destination.id == originId) continue
        if (portal.destination.findRoutePortal(position, id) == null) continue
        return portal
    }
    return null
}