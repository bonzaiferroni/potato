package ponder.potato.model.game

import kabinet.utils.random
import kotlinx.serialization.Serializable
import kotlin.collections.iterator

class GameEngine(
    override var state: GameState = GameState(),
    override var storage: GameStorage = GameStorage(),
    override val namingWay: NamingWay = NamingWay(),
) : Game {
    override val console = GameConsole()
    override val zones = mutableListOf<GameZone>()
    override val entities: MutableMap<Long, StateEntity<*>> = mutableMapOf()

    var entityIdSource = 1L
    var zoneIdSource = 1

    val graveyard = mutableListOf<StateEntity<*>>()
    val updating = mutableListOf<StateEntity<*>>()
    val opposers = mutableListOf<StateEntity<OpposerState>>()

    fun add(zone: GameZone, entityStates: Map<Long, EntityState>?) {
        zones.add(zone)
        zone.init(zoneIdSource++, this)
        if (entityStates == null) return
        addZoneStates(zone, entityStates)
    }

    private fun addZoneStates(zone: GameZone, entityStates: Map<Long, EntityState>) {
        for ((id, state) in entityStates) {
            val entity = state.toEntity()
            if (entity.position.zoneId != zone.id) continue
            spawn(zone, entity, entity.position.x, entity.position.y, id)
        }
    }

    inline fun <reified S : EntityState, reified E : StateEntity<S>> spawn(
        zone: GameZone,
        x: Float = Float.random(-BOUNDARY_X, BOUNDARY_X),
        y: Float = Float.random(-BOUNDARY_Y, BOUNDARY_Y),
        create: () -> E,
    ) {
        val entity = graveyard.firstNotNullOfOrNull { it as? E } ?: create()
        spawn(zone, entity, x, y)
    }

    inline fun <reified S : EntityState, reified E : StateEntity<S>> spawnIfAbsent(
        count: Int,
        zone: GameZone,
        x: Float = Float.random(-BOUNDARY_X, BOUNDARY_X),
        y: Float = Float.random(-BOUNDARY_Y, BOUNDARY_Y),
        create: () -> E,
    ) {
        if (entities.count { it is E && it.zone == zone } >= count) return
        spawn(zone, x, y, create)
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

    fun start() {
        for (zone in zones) {
            zone.start()
        }
        for (entity in entities.values) {
            entity.start()
        }
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
        // opposition
        for (entity in updating) {
            if (!entity.hasIntent(Intent.Oppose)) continue
            val opposer = entity.castIfState<OpposerState>() ?: continue
            val target = opposer.state.targetId?.let { entities[it]?.castIfState<SpiritState>() } ?: continue
            opposer.oppose(target)
            opposers.add(opposer)
        }

        // set storage limits
        storage.limits.clear()
        for (entity in updating) {
            val entityStorage = entity.state as? EntityStorageState ?: continue
            val resource = entityStorage.storedResource
            storage.limits[resource] = storage.readLimit(resource) + entityStorage.storedValue
        }

        for (entity in updating) {
            entity.update(delta)
        }
        for (entity in updating) {
            if (entity.state.isAlive) continue
            val maxSpirit = (entity.state as? SpiritState)?.maxSpirit ?: 10
            val level = (entity.state as? LevelState)?.level ?: 1
            val opposerCount = opposers.count { it.state.targetId == entity.id }
            val experience = maxSpirit * level / opposerCount.toDouble()
            for (opposer in opposers) {
                if (opposer.state.targetId != entity.id) continue
                val progressor = opposer.castIfState<LevelProgressState>() ?: continue
                progressor.state.addExperience(experience)
                if (progressor.isObserved) progressor.showEffect(ExperienceUp(experience))
            }
            entities.remove(entity.id)
            if (entity.isObserved) entity.showEffect(RaiseGhost())
            entity.recycle()
            graveyard.add(entity)
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

inline fun <reified T : GameZone> List<GameZone>.readOrNull() = this.firstOrNull() { it is T } as T?
inline fun <reified T : GameZone> List<GameZone>.read() = this.readOrNull<T>()
    ?: error("No zone found: ${T::class.simpleName}")