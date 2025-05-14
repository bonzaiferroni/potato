package ponder.potato.model.game

class Mine() : GameZone() {

    override fun start() {
        val cave = game.zones.read<Cave>()
        addPortal(cave, Direction.East)

        game.spawnIfAbsent(1, this, Point.origin) { Outcrop() }
        game.spawnIfAbsent(1, this, Direction.NorthWest.midPoint) { Vault() }
        game.spawnIfAbsent(1, this, Direction.SouthWest.midPoint) { DirtPile() }
    }

    override fun getStatus() = listOf(
        game.readResourceStatus(Resource.Dirt),
        game.readResourceStatus(Resource.Gold),
    )
}