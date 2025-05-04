package ponder.potato.model.game

import ponder.potato.model.game.entities.Entity
import ponder.potato.model.game.entities.EntityState
import ponder.potato.model.game.zones.Zone

typealias EntityMap = Map<Long, Entity>

inline fun <reified T> EntityMap.read(id: Long) = this[id] as? T


inline fun <reified T : Entity> EntityMap.findNearest(
    zone: Zone,
    position: Vector,
    isTarget: (T) -> Boolean,
): T? {
    var nearest: T? = null
    var nearestDistance = Float.MAX_VALUE
    for (entity in this.values) {
        if (entity.zone != zone || entity !is T || !isTarget(entity)) continue
        val distance = position.squaredDistanceTo(entity.position)
        if (distance > nearestDistance) continue
        nearest = entity
        nearestDistance = distance
    }
    return nearest
}

inline fun <reified S: EntityState> EntityMap.sumOf(block: (S) -> Double): Double {
    var value = 0.0
    for (entity in this.values) {
        val state = entity.state as? S ?: continue
        value += block(state)
    }
    return value
}