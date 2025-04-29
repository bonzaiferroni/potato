package ponder.potato.model.game.zones

import kotlinx.serialization.Serializable
import ponder.potato.model.game.LocalPoint
import ponder.potato.model.game.entities.Sprite

class Cave(
    // state: CaveState,
) : GameZone() {

    val sprites get() = game.entities.count() { it.value is Sprite && it.value.state.position.zoneId == id }

    fun manifestSprite() {
        game.spawn(this) { Sprite() }
    }
}