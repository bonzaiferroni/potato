package ponder.potato.model.game.entities

import ponder.potato.model.game.components.MutablePosition
import ponder.potato.model.game.components.Position
import ponder.potato.model.game.components.StateComponent
import ponder.potato.model.game.zones.GameZone
import ponder.potato.model.game.zones.Zone

interface Entity {
    val id: Long
    val zone: Zone
    val state: EntityState

    val game get() = zone.game
}

abstract class StateEntity<out T: EntityState>(
    override val state: T
): Entity {
    private var _zone: GameZone? = null
    override val zone get() = _zone ?: error("zone not initialized")

    val components = mutableListOf<StateComponent<*>>()
    override var id = 0L

    internal fun add(component: StateComponent<*>) {
        components.add(component)
    }

    fun enter(zone: GameZone) {
        _zone = zone
        for (component in components) {
            component.enter(zone)
        }
    }

    open fun init(id: Long) {
        this.id = id
        for (component in components) {
            component.init()
        }
    }

    open fun update(delta: Double) {
        for (component in components) {
            component.update(delta)
        }
    }
}

interface EntityState {
    val isAlive: Boolean
    val position: Position
}