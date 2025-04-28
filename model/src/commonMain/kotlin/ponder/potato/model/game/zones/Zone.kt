package ponder.potato.model.game.zones

import ponder.potato.model.game.entities.Entity
import ponder.potato.model.game.entities.StateEntity

interface Zone {
    val id: String
    val portals: List<Portal>
    val entities: List<Entity>
}

sealed class StateZone<State>(
    override val id: String,
    val state: State,
): Zone {
    override val portals = mutableListOf<Portal>()
    override val entities = mutableListOf<StateEntity<*>>()

    private val zones = mutableListOf<StateZone<*>>()

    fun <S, Z : StateZone<S>> add(zone: Z, x: Float, y: Float): Portal {
        zones.add(zone)
        val portal = ZonePortal(zone, x, y)
        portals.add(portal)
        return portal
    }

    internal fun add(entity: StateEntity<*>) {
        entities.add(entity)
        entity.zone = this
    }

    open fun update(delta: Double) {
        for (zone in zones) {
            zone.update(delta)
        }
        for (entity in entities) {
            entity.update(delta)
        }
    }
}