package ponder.potato.model.game.zones

import ponder.potato.model.game.BOUNDARY_X
import ponder.potato.model.game.Vector2

class Mine(
    val cave: Cave
): GameZone() {

    init {
        addPortal(cave, Vector2(BOUNDARY_X, 0f), Vector2(-BOUNDARY_X, 0f))
    }
}