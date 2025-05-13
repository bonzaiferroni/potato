package ponder.potato.model.game.zones

import kabinet.utils.random
import ponder.potato.model.game.BOUNDARY_X
import ponder.potato.model.game.Vector2
import ponder.potato.model.game.entities.Bard
import ponder.potato.model.game.read

class Mine(
    val cave: Cave,
): GameZone() {

    override fun init(id: Int, game: GameEngine) {
        super.init(id, game)
        addPortal(cave, Vector2(BOUNDARY_X, 0f), Vector2(-BOUNDARY_X, 0f))
    }
}