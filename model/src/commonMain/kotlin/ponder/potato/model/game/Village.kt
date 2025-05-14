package ponder.potato.model.game

class Village(
    val cave: Cave
) : GameZone() {

    val imps get() = game.entities.values.count() { it is Imp }

    override fun init(id: Int, game: GameEngine) {
        super.init(id, game)
        addPortal(cave, Vector2(-BOUNDARY, 0f), Vector2(BOUNDARY, 0f))
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