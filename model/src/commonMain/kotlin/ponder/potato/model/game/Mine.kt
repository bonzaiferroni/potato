package ponder.potato.model.game

class Mine(
    val cave: Cave,
) : GameZone() {

    override fun init(id: Int, game: GameEngine) {
        super.init(id, game)
        addPortal(cave, Vector2(BOUNDARY_X, 0f), Vector2(-BOUNDARY_X, 0f))
        game.spawn(this, 0f, 0f) { Outcrop() }
        game.spawn(this, -BOUNDARY_X / 2, BOUNDARY_Y / 2) { Vault() }
        game.spawn(this, -BOUNDARY_X / 2, -BOUNDARY_Y / 2) { DirtPile() }
    }

    override fun getStatus() = listOf(
        game.readResourceStatus(Resource.Dirt),
        game.readResourceStatus(Resource.Gold),
    )
}