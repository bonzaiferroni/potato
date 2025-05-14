package ponder.potato.model.game

interface Zone {
    val id: Int
    val portals: List<Portal>
    val game: Game
    val name: String
    val console: GameConsole
}

sealed class GameZone(): Zone {

    private var _id: Int? = null
    private var _game: GameEngine? = null
    override val console get() = game.console

    override val id: Int get() = _id ?: error("id not initialized")
    override val game: GameEngine get() = _game ?: error("game not initialized")
    val resources get() = game.storage
    override val name get() = this::class.simpleName ?: error("Zone must not be anonymous")

    override val portals = mutableListOf<Portal>()
    open fun getZoneActions(): List<ZoneAction> = emptyList()
    open fun getStatus(): List<ZoneStatus> = emptyList()

    open fun init(id: Int, game: GameEngine) {
        _id = id
        _game = game
    }

    open fun start() { }

    open fun update(delta: Double) { }

    inline fun <reified S : EntityState, reified E : StateEntity<S>> spawnIfAbsent(
        point: Point,
        create: () -> E,
    ) {
        val count = game.entities.count {
            it is E && it.zone == this && it.position.x == point.x && it.position.y == point.y
        }
        if (count > 0) return
        game.spawn(
            zone = this,
            x = point.x,
            y = point.y,
            create = create
        )
    }

    fun addPortal(
        zone: GameZone,
        point: Point,
        remotePoint: Point,
        isBidirectional: Boolean = true
    ) {
        val portal = Portal(zone, point, this.id, remotePoint)
        portals.add(portal)
        if (isBidirectional) {
            zone.portals.add(Portal(this, remotePoint, zone.id, point))
        }
    }

    fun addPortal(
        zone: GameZone,
        direction: Direction,
        isBidirectional: Boolean = true
    ) = addPortal(zone, direction.boundaryPoint, direction.opposite.boundaryPoint, isBidirectional)

    fun getEntityActions(): List<EntityAction> {
        val actions = mutableListOf<EntityAction>()
        for (entity in game.entities.values) {
            if (entity.zone != this) continue
            for (ability in entity.abilities) {
                val action = ability() ?: continue
                actions.add(action)
            }
        }
        return actions
    }

    inline fun <reified T: EntityState> readFirstOrNull() =
        game.entities.values.firstNotNullOfOrNull { if (it.zone == this) it.castIfState<T>() else null }
}