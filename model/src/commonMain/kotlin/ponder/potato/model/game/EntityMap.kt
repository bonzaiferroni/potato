package ponder.potato.model.game

import kotlin.reflect.KClass

typealias EntityMap = Map<Long, Entity>

// read entities

inline fun <reified T: Entity> EntityMap.readEntity() = this.firstNotNullOfOrNull() { (_, entity) -> entity as? T }
inline fun <reified T: Entity> EntityMap.readEntity(id: Long) = this[id] as? T

inline fun <reified T: EntityState> EntityMap.readWithState(id: Long) =
    this[id]?.castIfState<T>()
inline fun <reified T: EntityState> EntityMap.readWithState(predicate: (StateEntity<T>) -> Boolean) =
    this.firstNotNullOfOrNull { (_, entity) ->
        val entity = entity.castIfState<T>() ?: return@firstNotNullOfOrNull null
        if (predicate(entity)) entity else null
    }

inline fun <reified T> EntityMap.readWithZone(zone: Zone) = this.readWithZone<T>(zone.id)

inline fun <reified T> EntityMap.readWithZone(zoneId: Int) = this.values.firstNotNullOfOrNull {
    if (it.position.zoneId != zoneId) null
    else it as? T
}

// find nearest

inline fun <reified T : Entity> EntityMap.findNearest(
    position: Position,
    range: Float = Float.MAX_VALUE,
    isTarget: (T) -> Boolean
): T? {
    var nearest: T? = null
    var nearestDistance = Float.MAX_VALUE
    val rangeSquared = if (range == Float.MAX_VALUE) Float.MAX_VALUE else range * range
    for (entity in this.values) {
        if (entity.position.zoneId != position.zoneId || entity !is T || !isTarget(entity)) continue
        val distance = position.squaredDistanceTo(entity.position)
        if (distance > nearestDistance || distance >= rangeSquared) continue
        nearest = entity
        nearestDistance = distance
    }
    return nearest
}

// sumOf

inline fun <reified S : EntityState> EntityMap.sumOf(block: (S) -> Double): Double {
    var value = 0.0
    for (entity in this.values) {
        val state = entity.state as? S ?: continue
        value += block(state)
    }
    return value
}

inline fun <reified S : EntityState> EntityMap.sumOf(where: (StateEntity<S>) -> Boolean, block: (S) -> Double): Double {
    var value = 0.0
    for (entity in this.values) {
        val castEntity = entity.castIfState<S>() ?: continue
        if (!where(castEntity)) continue
        val state = entity.state as? S ?: continue
        value += block(state)
    }
    return value
}

inline fun EntityMap.count(block: (Entity) -> Boolean) =
    this.values.count { block(it) }

fun EntityMap.count(kClass: KClass<*>, zoneId: Int) =
    this.values.count { it::class == kClass && it.position.zoneId == zoneId }

inline fun <reified E : StateEntity<*>> EntityMap.count() = this.values.count { it is E }