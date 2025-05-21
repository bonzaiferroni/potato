package ponder.potato.model.game

import kotlin.reflect.KClass

typealias EntityMap = Map<Long, Entity>

inline fun <reified T> EntityMap.read(id: Long) = this[id] as? T
inline fun <reified T: EntityState> EntityMap.readAsStateEntity(id: Long) = this[id]?.castIfState<T>()
inline fun <reified T> EntityMap.read() = this.values.firstNotNullOfOrNull() { it as? T }
inline fun <reified T> EntityMap.read(block: (T) -> Boolean) = this.values.firstNotNullOfOrNull {
    val entity = it as? T ?: return@firstNotNullOfOrNull null
    if (block(entity)) entity else null
}
inline fun <reified T> EntityMap.read(zone: Zone) = this.values.firstNotNullOfOrNull {
    if (it.zone != zone) null
    else it as? T
}
inline fun <reified T> EntityMap.readWithZoneId(zoneId: Int) = this.values.firstNotNullOfOrNull {
    if (it.position.zoneId != zoneId) null
    else it as? T
}

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

inline fun <reified S: EntityState> EntityMap.sumOf(block: (S) -> Double): Double {
    var value = 0.0
    for (entity in this.values) {
        val state = entity.state as? S ?: continue
        value += block(state)
    }
    return value
}

inline fun <reified S: EntityState> EntityMap.sumOf(where: (StateEntity<S>) -> Boolean, block: (S) -> Double): Double {
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

inline fun <reified E: StateEntity<*>> EntityMap.count() = this.values.count { it is E }