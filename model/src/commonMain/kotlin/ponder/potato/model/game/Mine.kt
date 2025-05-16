package ponder.potato.model.game

class Mine() : GameZone() {

    override fun start() {
        val cave = game.zones.read<Cave>()
        addPortal(cave, Direction.East)

        spawnIfAbsent(Point.origin) { Outcrop() }
        spawnIfAbsent(Direction.NorthWest.midPoint) { Vault() }
        spawnIfAbsent(Direction.SouthWest.midPoint) { DirtPile() }
        spawnIfAbsent(Point.origin) { Bot() }

        if (game.programs.isEmpty()) {
            game.programs[0] = Program("mine", mutableListOf(MineTarget(id)))
        }
    }

    override fun getStatus() = listOf(
        game.readResourceStatus(Resource.Dirt),
        game.readResourceStatus(Resource.Gold),
    )
}