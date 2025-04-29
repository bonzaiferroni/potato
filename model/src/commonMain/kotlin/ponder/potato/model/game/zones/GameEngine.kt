package ponder.potato.model.game.zones

import kotlinx.serialization.Serializable
import ponder.potato.model.game.GameResources
import ponder.potato.model.game.Resources
import ponder.potato.model.game.entities.Entity
import ponder.potato.model.game.entities.Potato
import ponder.potato.model.game.entities.StateEntity

interface Game {
    val state: GameState
    val zones: List<Zone>
    val entities: Map<Long, Entity>
    val resources: Resources
    val map: GameMap
}

class GameEngine(
    override var state: GameState = GameState(),
    override var resources: GameResources = GameResources(),
    override val map: GameMap
): Game {

    override val zones = mutableListOf<GameZone>()
    override val entities = mutableMapOf<Long, StateEntity<*>>()

    var entityIdSource = 1L
    var zoneIdSource = 1

    val graveyard = mutableListOf<StateEntity<*>>()

    fun add(zone: GameZone) {
        zones.add(zone)
        zone.init(zoneIdSource++, this)
    }

    inline fun <reified S, reified E: StateEntity<S>> spawn(create: () -> E) {
        val ghost = graveyard.firstNotNullOfOrNull {
            it as? E ?: return@firstNotNullOfOrNull null
        } ?: create()
        spawn(ghost)
    }

    fun spawn(entity: StateEntity<*>) {
        entity.init(entityIdSource++)
        entities[entity.id] = entity
    }

    fun update(delta: Double) {
        state.time += delta
        state.tick++
        state.delta = delta
        for (zone in zones) {
            zone.update(delta)
        }
        for ((_, entity) in entities) {
            entity.update(delta)
        }
    }

}

@Serializable
data class GameState(
    var tick: Long = 0L,
    var time: Double = 0.0,
    var delta: Double = 1.0,
)