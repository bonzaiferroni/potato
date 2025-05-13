package ponder.potato.model.game

class Mine(
    val cave: Cave,
): GameZone() {

    override fun init(id: Int, game: GameEngine) {
        super.init(id, game)
        addPortal(cave, Vector2(BOUNDARY_X, 0f), Vector2(-BOUNDARY_X, 0f))
    }
}