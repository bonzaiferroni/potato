package ponder.potato.model.game.zones

import ponder.potato.model.game.BOUNDARY_X
import ponder.potato.model.game.BOUNDARY_Y
import ponder.potato.model.game.entities.Bard
import ponder.potato.model.game.entities.Potato
import ponder.potato.model.game.entities.Shroom
import ponder.potato.model.game.entities.Sprite
import ponder.potato.model.game.factorValue

class Cave : GameZone() {

    val spriteCount get() = game.entities.values.count() { it is Sprite && it.position.zoneId == id }
    val maxSpriteCount get() = listOf(0, 5, 10, 20)[game.dreamLevel]
    val spriteCost get() = factorValue(100, spriteCount + 1, 1.2)
    val canDreamSprite get() = resources.aether >= spriteCost && spriteCount < maxSpriteCount
    val shroomCount get() = game.entities.values.count() { it is Shroom && it.position.zoneId == id }
    val maxShroomCount get() = listOf(0, 5, 10, 20)[game.dreamLevel]
    val shroomCost get() = factorValue(300, shroomCount + 1, 1.2)
    val canDreamShroom get() = resources.aether >= shroomCost && shroomCount < maxShroomCount
    val bardCount get() = game.entities.values.count() { it is Bard }
    val canDreamBard get() = resources.aether >= BARD_COST && bardCount == 0

    override fun init(id: Int, game: GameEngine) {
        super.init(id, game)
        game.spawn(this, 0f, 0f) { Potato() }
//        game.spawn(this) { Imp() }
//        game.spawn(this, BOUNDARY_X, BOUNDARY_Y) { Potato() }
//        game.spawn(this, -BOUNDARY_X, BOUNDARY_Y) { Potato() }
//        game.spawn(this, BOUNDARY_X, -BOUNDARY_Y) { Potato() }
//        game.spawn(this, -BOUNDARY_X, -BOUNDARY_Y) { Potato() }
    }

    fun manifestSprite() {
        if (!canDreamSprite) return
        resources.aether -= spriteCost
        game.spawn(this) { Sprite() }
    }

    fun manifestShroom() {
        if (!canDreamShroom) return
        resources.aether -= shroomCost
        game.spawn(this) { Shroom() }
    }

    fun dreamBard() {
        if (!canDreamBard) return
        resources.aether -= BARD_COST
        game.spawn(this, BOUNDARY_X / 2, BOUNDARY_Y / 2) { Bard() }
    }
}

const val BARD_COST = 2000.0