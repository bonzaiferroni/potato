package ponder.potato.model.game

import kotlinx.serialization.Serializable

@Serializable
class Program(
    var name: String,
    val statements: MutableList<Statement> = mutableListOf()
) {
    fun execute(game: Game, bot: Bot, delta: Double) {
        if (statements.isEmpty()) return
        val instructionId = bot.state.instructionId ?: 0
        val statement = statements[instructionId]
        val execution = statement.instruction.execute(game, bot, delta)
        val response = statement.responses.firstOrNull { it.execution == execution }
        if (response != null) {
            bot.state.programId = response.programId
            bot.state.instructionId = 0
            return
        }
        // bot.showEffect { Exec(execution) }
        if (execution == Execution.Complete) {
            val nextId = instructionId + 1
            bot.state.instructionId = if (nextId < statements.size) nextId else 0
        }
    }

    fun addInstruction(instruction: Instruction) {
        statements.add(Statement(instruction))
    }
}

@Serializable
class Statement(
    val instruction: Instruction,
    val responses: List<InstructionResponse> = emptyList()
)

@Serializable
data class InstructionResponse(
    val execution: Execution,
    val programId: Int,
)