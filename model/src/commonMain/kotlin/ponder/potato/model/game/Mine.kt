package ponder.potato.model.game

class Mine() : GameZone() {

    override fun start() {
        val cave = game.zones.read<Cave>()
        addPortal(cave, Direction.East)

        spawnIfAbsent(Point.origin) { Outcrop() }
        spawnIfAbsent(Direction.NorthWest.midPoint) { Vault() }
        spawnIfAbsent(Direction.SouthWest.midPoint) { DirtPile() }
        spawnIfAbsent(Point.origin) { Bot() }
    }

    fun dreamBot() {
        game.spawn(this, Direction.SouthEast.midPoint) { Bot() }
    }

    override fun getZoneActions() = listOf(
        ZoneAction(
            ability = ZoneAbility.DreamBot,
            status = null,
            cost = 100.0,
            currentResource = game.storage.aether,
            count = game.entities.count<Bot>(),
            maxCount = 5,
            block = ::dreamBot,
        )
    )

    override fun getStatus() = listOf(
        game.readResourceStatus(Resource.Dirt),
        game.readResourceStatus(Resource.Gold),
    )
}