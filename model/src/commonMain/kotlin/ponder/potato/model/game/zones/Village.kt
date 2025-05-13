package ponder.potato.model.game.zones

import ponder.potato.model.game.*
import ponder.potato.model.game.entities.Imp

class Village(
    val cave: Cave
) : GameZone() {

    val imps get() = game.entities.values.count() { it is Imp }

    init {
        addPortal(cave, Vector2(-BOUNDARY_X, 0f), Vector2(BOUNDARY_X, 0f))
    }

    override fun update(delta: Double) {
        super.update(delta)
        val spriteCount = cave.spriteCount
        val impCount = imps
        if (spriteCount > (impCount + 1) * 5) {
            game.spawn(this) { Imp() }
        }
    }
}