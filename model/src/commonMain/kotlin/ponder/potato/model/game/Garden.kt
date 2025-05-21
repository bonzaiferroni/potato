package ponder.potato.model.game

class Garden(): GameZone(), InstructionSource {
    override fun start() {
        val mine = game.zones.read<Mine>()
        addPortal(mine, Direction.East)

        spawnIfAbsent(Direction.SouthEast.midPoint) { GardenBed() }
    }

    override fun addInstructions(instructions: MutableList<Instruction>) {
        instructions.add(FillResource(id, Resource.Dirt))
    }
}