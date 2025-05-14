package ponder.potato.model.game

class Mine(
    val cave: Cave,
) : GameZone() {

    override fun start() {
        addPortal(cave, Vector2(BOUNDARY_X, 0f), Vector2(-BOUNDARY_X, 0f))

        game.spawnIfAbsent(1, this, 0f, 0f) { Outcrop() }
        game.spawnIfAbsent(1, this, -BOUNDARY_X / 2, BOUNDARY_Y / 2) { Vault() }
        game.spawnIfAbsent(1, this, -BOUNDARY_X / 2, -BOUNDARY_Y / 2) { DirtPile() }
    }

    override fun getStatus() = listOf(
        game.readResourceStatus(Resource.Dirt),
        game.readResourceStatus(Resource.Gold),
    )
}