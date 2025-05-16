package ponder.potato.model.game

import kotlinx.serialization.Serializable

@Serializable
class Program(
    var name: String,
    val instructions: MutableList<Instruction> = mutableListOf()
) {
    fun execute(game: Game, bot: Bot, delta: Double) {
        if (instructions.isEmpty()) return
        val instructionId = bot.state.instructionId ?: 0
        val instruction = instructions[instructionId]
        val execution = instruction.execute(game, bot, delta)
        // bot.showEffect { Exec(execution) }
        if (execution == Execution.Complete) {
            val nextId = instructionId + 1
            bot.state.instructionId = if (nextId < instructions.size) nextId else 0
        }

    }
}