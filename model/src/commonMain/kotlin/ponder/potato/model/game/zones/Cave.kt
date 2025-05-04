package ponder.potato.model.game.zones

import ponder.potato.model.game.BOUNDARY_X
import ponder.potato.model.game.BOUNDARY_Y
import ponder.potato.model.game.entities.Imp
import ponder.potato.model.game.entities.Potato
import ponder.potato.model.game.entities.Sprite

class Cave : GameZone() {

    val sprites get() = game.entities.values.count() { it is Sprite && it.position.zoneId == id }

    override fun init(id: Int, game: GameEngine) {
        super.init(id, game)
        game.spawn(this) { Potato() }
//        game.spawn(this) { Imp() }
//        game.spawn(this, BOUNDARY_X, BOUNDARY_Y) { Potato() }
//        game.spawn(this, -BOUNDARY_X, BOUNDARY_Y) { Potato() }
//        game.spawn(this, BOUNDARY_X, -BOUNDARY_Y) { Potato() }
//        game.spawn(this, -BOUNDARY_X, -BOUNDARY_Y) { Potato() }
    }

    fun manifestSprite() {
        game.spawn(this) { Sprite() }
    }
}