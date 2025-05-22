package ponder.potato.ui

import ponder.potato.GameService
import ponder.potato.ProgramProfileRoute
import ponder.potato.model.game.Game
import ponder.potato.model.game.GameState
import ponder.potato.model.game.Instruction
import ponder.potato.model.game.Statement
import ponder.potato.model.game.readAllInstructions
import pondui.ui.core.StateModel

class ProgramProfileModel(
    route: ProgramProfileRoute,
    private val gameService: GameService = GameService()
): StateModel<ProgramProfileState>(ProgramProfileState(route.programId)) {

    val availableInstructions: List<Instruction>

    init {
        availableInstructions = game.readAllInstructions()
        val availableItems = availableInstructions.mapIndexed { id, it -> it.toItem(id, game) }
        setState { it.copy(availableInstructions = availableItems) }
        load()
    }

    val game get() = gameService.game

    fun update(gameState: GameState) {
        load()
    }

    fun toggleAddItem() {
        setState { it.copy(isAddingItem = !it.isAddingItem) }
    }

    fun addInstruction(id: Int) {
        val instruction = availableInstructions[id]
        val program = game.programs[stateNow.programId] ?: return
        program.addInstruction(instruction)
    }

    fun load(programId: Int = stateNow.programId) {
        val program = game.programs[programId] ?: return
        setState { it.copy(
            programName = program.name,
            instructions = program.statements.toInstructionItems(game)
        ) }
    }
}

data class ProgramProfileState(
    val programId: Int = 0,
    val programName: String = "",
    val instructions: List<InstructionItem> = emptyList(),
    val isAddingItem: Boolean = false,
    val availableInstructions: List<InstructionItem> = emptyList()
)

data class InstructionItem(
    val id: Int,
    val scopeName: String?,
    val name: String,
    val parameterName: String?
)

fun Instruction.toItem(id: Int, game: Game) = when (this) {
    else -> InstructionItem(
        id = id,
        scopeName = this.getScopeName(game),
        name = this.name,
        parameterName = this.getParameterName(game)
    )
}

fun List<Statement>.toInstructionItems(game: Game) = this.mapIndexed { id, it -> it.instruction.toItem(id, game) }