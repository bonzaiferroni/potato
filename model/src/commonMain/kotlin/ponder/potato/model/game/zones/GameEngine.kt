package ponder.potato.model.game.zones

import kabinet.utils.random
import kotlinx.serialization.Serializable
import ponder.potato.model.game.BOUNDARY_X
import ponder.potato.model.game.BOUNDARY_Y
import ponder.potato.model.game.ExperienceUp
import ponder.potato.model.game.GameResources
import ponder.potato.model.game.RaiseGhost
import ponder.potato.model.game.components.LevelState
import ponder.potato.model.game.components.OpposerState
import ponder.potato.model.game.components.ProgressState
import ponder.potato.model.game.components.SpiritState
import ponder.potato.model.game.entities.Entity
import ponder.potato.model.game.entities.EntityState
import ponder.potato.model.game.entities.StateEntity
import ponder.potato.model.game.oppose

class GameEngine(
    override var state: GameState = GameState(),
    override var resources: GameResources = GameResources(),
    override val namingWay: NamingWay = NamingWay(),
    val entityStates: Map<Long, EntityState>? = null
) : Game {

    override val zones = mutableListOf<GameZone>()
    override val entities: MutableMap<Long, StateEntity<*>> = mutableMapOf()

    var entityIdSource = 1L
    var zoneIdSource = 1

    val graveyard = mutableListOf<StateEntity<*>>()
    val updating = mutableListOf<StateEntity<*>>()
    val opposers = mutableListOf<StateEntity<OpposerState>>()

    fun add(zone: GameZone) {
        zones.add(zone)
        zone.init(zoneIdSource++, this)
        addZoneStates(zone)
    }

    private fun addZoneStates(zone: GameZone) {
        if (entityStates == null) return
        for ((id, state) in entityStates) {
            val entity = state.toEntity()
            if (entity.position.zoneId != zone.id) continue
            spawn(zone, entity, entity.position.x, entity.position.y, id)
        }
    }

    inline fun <reified S, reified E : StateEntity<S>> spawn(
        zone: GameZone,
        x: Float = Float.random(-BOUNDARY_X, BOUNDARY_X),
        y: Float = Float.random(-BOUNDARY_Y, BOUNDARY_Y),
        create: () -> E,
    ) {
        val ghost = graveyard.firstNotNullOfOrNull {
            it as? E ?: return@firstNotNullOfOrNull null
        } ?: create()
        spawn(zone, ghost, x, y)
    }

    fun spawn(
        zone: GameZone,
        entity: StateEntity<*>,
        x: Float = Float.random(-BOUNDARY_X, BOUNDARY_X),
        y: Float = Float.random(-BOUNDARY_Y, BOUNDARY_Y),
        id: Long = entityIdSource,
    ) {
        entities[id] = entity
        entity.enter(zone)
        entity.state.position.x = x
        entity.state.position.y = y
        entity.init(id)
        entityIdSource = maxOf(id + 1, entityIdSource)
    }

    fun update(delta: Double) {
        state.time += delta
        state.tick++
        state.delta = delta
        for (zone in zones) {
            zone.update(delta)
        }
        opposers.clear()
        updating.clear()
        updating.addAll(entities.values)
        for (entity in updating) {
            val opposer = entity.castIfState<OpposerState>() ?: continue
            val target = opposer.state.oppositionId?.let { entities[it]?.castIfState<SpiritState>() } ?: continue
            opposer.oppose(target)
            opposers.add(opposer)
        }
        for (entity in updating) {
            entity.update(delta)
        }
        for (entity in updating) {
            if (entity.state.isAlive) continue
            val maxSpirit = (entity.state as? SpiritState)?.maxSpirit ?: 10
            val level = (entity.state as? LevelState)?.level ?: 1
            val opposerCount = opposers.count { it.state.oppositionId == entity.id }
            val experience = maxSpirit * level / opposerCount.toDouble()
            for (opposer in opposers) {
                val progressor = opposer.castIfState<ProgressState>() ?: continue
                progressor.state.addExperience(experience)
                progressor.showEffect { ExperienceUp(experience) }
            }
            entities.remove(entity.id)
            entity.showEffect { RaiseGhost() }
        }
    }

}

@Serializable
data class GameState(
    var tick: Long = 0L,
    var time: Double = 0.0,
    var delta: Double = 1.0,
)

@Suppress("UNCHECKED_CAST")
inline fun <reified T : EntityState> StateEntity<*>.castIfState(): StateEntity<T>? {
    return if (state is T) this as StateEntity<T> else null
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : EntityState> Entity.castIfState(): StateEntity<T>? {
    return if (state is T) this as StateEntity<T> else null
}