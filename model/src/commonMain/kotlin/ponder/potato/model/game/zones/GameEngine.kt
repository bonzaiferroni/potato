package ponder.potato.model.game.zones

import kotlinx.serialization.Serializable
import ponder.potato.model.game.Despirit
import ponder.potato.model.game.GameResources
import ponder.potato.model.game.OpposeEffect
import ponder.potato.model.game.RaiseGhost
import ponder.potato.model.game.Resources
import ponder.potato.model.game.components.OpposerState
import ponder.potato.model.game.components.SpiritState
import ponder.potato.model.game.entities.Entity
import ponder.potato.model.game.entities.Potato
import ponder.potato.model.game.entities.StateEntity
import ponder.potato.model.game.oppose

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
) : Game {

    override val zones = mutableListOf<GameZone>()
    override val entities = mutableMapOf<Long, StateEntity<*>>()

    var entityIdSource = 1L
    var zoneIdSource = 1

    val graveyard = mutableListOf<StateEntity<*>>()
    val ghostIds = mutableListOf<Long>()
    val manifesting = mutableListOf<StateEntity<*>>()

    fun add(zone: GameZone) {
        zones.add(zone)
        zone.init(zoneIdSource++, this)
    }

    inline fun <reified S, reified E : StateEntity<S>> spawn(
        zone: GameZone,
        create: () -> E,
    ) {
        val ghost = graveyard.firstNotNullOfOrNull {
            it as? E ?: return@firstNotNullOfOrNull null
        } ?: create()
        spawn(zone, ghost)
    }

    fun spawn(
        zone: GameZone,
        entity: StateEntity<*>,
    ) {
        manifesting.add(entity)
        entity.init(entityIdSource++)
        entity.enter(zone)
    }

    @Suppress("UNCHECKED_CAST")
    fun update(delta: Double) {
        state.time += delta
        state.tick++
        state.delta = delta
        for (zone in zones) {
            zone.update(delta)
        }
        for (entity in manifesting) {
            entities[entity.id] = entity
        }
        manifesting.clear()
        for (entity in entities.values) {
            val opposer = entity as? StateEntity<OpposerState> ?: continue
            val target = opposer.state.oppositionId?.let { entities[it] as? StateEntity<SpiritState> } ?: continue
            entity.oppose(target)
        }
        for (entity in entities.values) {
            entity.update(delta)
        }
        for (entity in entities.values) {
            if (entity.state.isAlive) continue
            ghostIds.add(entity.id)
            entity.showEffect { RaiseGhost() }
        }
        for (id in ghostIds) {
            entities.remove(id)
        }
        ghostIds.clear()
    }

}

@Serializable
data class GameState(
    var tick: Long = 0L,
    var time: Double = 0.0,
    var delta: Double = 1.0,
)