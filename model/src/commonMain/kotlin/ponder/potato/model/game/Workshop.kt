package ponder.potato.model.game

class Workshop: GameZone() {

    override fun start() {
        val mine = game.zones.read<Mine>()
        addPortal(mine, Direction.South)
        spawnIfAbsent(Point.origin) { Engineer() }
    }

    override fun getZoneActions(): List<ZoneAction> = listOf(
        ZoneAction(
            ability = ZoneAbility.BuySeed,
            status = null,
            cost = 200.0,
            currentResource = game.storage.readQuantity(Resource.Gold),
        ) {
            val removed = game.storage.removeQuantity(Resource.Gold, 200.0)
            if (removed) {
                game.storage.addQuantity(Item.Seed, 1)
                println("bought a seed")
            }
        }
    )
}