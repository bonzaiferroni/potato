package ponder.potato.model.game

class Garden(): GameZone(), InstructionSource {
    override fun start() {
        val mine = game.zones.read<Mine>()
        addPortal(mine, Direction.East)

        spawnIfAbsent(Direction.SouthEast.midPoint) { GardenBed() }
    }

    override fun addInstructions(list: MutableList<Instruction>) {
        list.add(FillResource(id, Resource.Dirt))
    }
}