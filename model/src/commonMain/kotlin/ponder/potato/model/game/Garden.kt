package ponder.potato.model.game

class Garden(): GameZone() {
    override fun start() {
        val mine = game.zones.read<Mine>()
        addPortal(mine, Direction.East)

        spawnIfAbsent(Direction.SouthEast.midPoint) { GardenBed() }
    }
}