package ponder.potato.ui

import ponder.potato.GameService
import ponder.potato.ProgramProfileRoute
import ponder.potato.model.game.Game
import ponder.potato.model.game.GameState
import ponder.potato.model.game.Instruction
import pondui.ui.core.StateModel

class ProgramProfileModel(
    route: ProgramProfileRoute,
    private val gameService: GameService = GameService()
): StateModel<ProgramProfileState>(ProgramProfileState(route.programId)) {

    init {
        load()
    }

    val game get() = gameService.game

    fun update(gameState: GameState) {
        load()
    }

    fun load(programId: Int = stateNow.programId) {
        val program = game.programs[programId] ?: return
        val instructions = program.instructions.map { it.toItem(game) }
        setState { it.copy(
            programName = program.name,
            instructions = instructions
        ) }
    }
}

data class ProgramProfileState(
    val programId: Int = 0,
    val programName: String = "",
    val instructions: List<InstructionItem> = emptyList()
)

data class InstructionItem(
    val scopeName: String?,
    val name: String,
    val parameterName: String?
)

fun Instruction.toItem(game: Game) = when (this) {
    else -> InstructionItem(
        scopeName = this.getScopeName(game),
        name = this.name,
        parameterName = this.getParameterName(game)
    )
}