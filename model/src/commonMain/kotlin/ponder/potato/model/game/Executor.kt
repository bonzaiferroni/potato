package ponder.potato.model.game

class Executor(
    override val entity: Bot
): StateComponent<ExecutorState>() {

    override fun update(delta: Double) {
        val program = state.programId?.let { game.programs[it] } ?: return
        program.execute(game, entity, delta)
    }
}

interface ExecutorState: EntityState {
    var programId: Int?
    var instructionId: Int?
}