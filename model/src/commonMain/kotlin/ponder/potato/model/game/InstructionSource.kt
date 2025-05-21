package ponder.potato.model.game

interface InstructionSource {
    fun addInstructions(instructions: MutableList<Instruction>)
}