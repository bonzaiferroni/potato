package ponder.potato.model.game.zones

import ponder.potato.model.game.entities.Sprite

class Cave : GameZone() {

    val sprites get() = game.entities.values.count() { it is Sprite && it.position.zoneId == id }

    fun manifestSprite() {
        game.spawn(this) { Sprite() }
    }
}