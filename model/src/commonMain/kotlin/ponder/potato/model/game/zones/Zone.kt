package ponder.potato.model.game.zones

import ponder.potato.model.game.Vector

interface Zone {
    val id: Int
    val portals: List<Portal>
    val game: Game
}

sealed class GameZone(): Zone {

    private var _id: Int? = null
    private var _game: GameEngine? = null

    override val id: Int get() = _id ?: error("id not initialized")
    override val game: GameEngine get() = _game ?: error("game not initialized")

    override val portals = mutableListOf<Portal>()

    open fun init(id: Int, game: GameEngine) {
        _id = id
        _game = game
    }

    open fun update(delta: Double) {

    }

    fun <Z : GameZone> addPortal(
        zone: Z,
        local: Vector,
        remote: Vector,
        isBidirectional: Boolean = true
    ): Z {
        val portal = Portal(zone, local.x, local.y, remote.x, remote.y)
        portals.add(portal)
        if (isBidirectional) {
            zone.portals.add(Portal(this, remote.x, remote.y, local.x, local.y))
        }
        return zone
    }
}