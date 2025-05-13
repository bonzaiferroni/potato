package ponder.potato.model.game

import kabinet.utils.toMetricString

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
//        game.spawn(this, BOUNDARY_X, BOUNDARY_Y) { Bard() }
//        game.spawn(this, -BOUNDARY_X, BOUNDARY_Y) { Bard() }
//        game.spawn(this, BOUNDARY_X, -BOUNDARY_Y) { Bard() }
//        game.spawn(this, -BOUNDARY_X, -BOUNDARY_Y) { Bard() }
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

    override fun getZoneActions() = listOf(
        ZoneAction(
            ability = ZoneAbility.DreamSprite,
            status = if (spriteCount > 0 && maxSpriteCount > 0) {
                "This dream has $spriteCount out of $maxSpriteCount possible sprites that provide" +
                        " ${game.readSpriteAether().toMetricString()} of The Aether at the end of each dream."
            } else null,
            cost = 500.0,
            currentResource = resources.aether,
            count = spriteCount,
            maxCount = maxSpriteCount,
            block = ::manifestSprite
        ),
        ZoneAction(
            ability = ZoneAbility.DreamShroom,
            status = if (shroomCount > 0 && maxShroomCount > 0) {
                "You have $shroomCount shrooms that hold ${game.readShroomStorage().toMetricString()} additional Aether."
            } else null,
            cost = 1000.0,
            currentResource = resources.aether,
            count = shroomCount,
            maxCount = maxShroomCount,
            block = ::manifestShroom
        ),
    )

    override fun getStatus() = listOf(
        IntValue("Dream level", game.dreamLevel),
        game.readResourceStatus(Resource.Aether)
    )
}

const val BARD_COST = 2000.0