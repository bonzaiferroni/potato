package ponder.potato.model.game.entities

import ponder.potato.model.game.components.Component
import ponder.potato.model.game.zones.Zone

interface Entity {
    val zone: Zone
    val state: EntityState
}

abstract class StateEntity<T: EntityState>(
    override var zone: Zone,
    override var state: T
): Entity {
    val components = mutableListOf<Component>()

    internal fun add(component: Component) {
        components.add(component)
    }

    inline fun <reified T> get() = components.firstOrNull { it is T }

    open fun update(delta: Double) {
        for (component in components) {
            component.update(delta)
        }
    }
}

interface EntityState {

}