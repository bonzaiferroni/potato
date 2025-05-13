package ponder.potato.model.game

interface Zone {
    val id: Int
    val portals: List<Portal>
    val game: Game
    val name: String
}

sealed class GameZone(): Zone {

    private var _id: Int? = null
    private var _game: GameEngine? = null

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

    open fun update(delta: Double) { }

    fun <Z : GameZone> addPortal(
        zone: Z,
        local: Point,
        remote: Point,
        isBidirectional: Boolean = true
    ): Z {
        val portal = Portal(zone, local.x, local.y, this.id, remote.x, remote.y)
        portals.add(portal)
        if (isBidirectional) {
            zone.portals.add(Portal(this, remote.x, remote.y, zone.id,  local.x, local.y))
        }
        return zone
    }

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

    inline fun <reified T: EntityState> readFirstOrNull() = game.entities.values.firstNotNullOfOrNull { it.castIfState<T>() }
}

