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

    fun <Z : GameZone> addPortal(zone: Z, local: Vector): Z {
        val portal = Portal(zone, local.x, local.y)
        portals.add(portal)
        return zone
    }
}