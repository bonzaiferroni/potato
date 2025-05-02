package ponder.potato.model.game.zones

import ponder.potato.model.game.entities.Imp
import ponder.potato.model.game.entities.Sprite

class Cave : GameZone() {

    val sprites get() = game.entities.values.count() { it is Sprite && it.position.zoneId == id }

    override fun init(id: Int, game: GameEngine) {
        super.init(id, game)
        game.spawn(this) { Imp() }
    }

    fun manifestSprite() {
        game.spawn(this) { Sprite() }
    }
}