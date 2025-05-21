package ponder.potato.model.game

interface InstructionSource {
    fun addInstructions(list: MutableList<Instruction>)
}