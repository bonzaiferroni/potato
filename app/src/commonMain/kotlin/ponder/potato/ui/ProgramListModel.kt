package ponder.potato.ui

import ponder.potato.GameService
import ponder.potato.model.game.GameState
import pondui.ui.core.StateModel

class ProgramListModel(
    private val gameService: GameService = GameService()
): StateModel<ProgramListState>(ProgramListState()) {

    val game get() = gameService.game

    fun update(gameState: GameState) {
        val items = game.programs.map {
            ProgramItem(
                programId = it.key,
                programName = it.value.name,
                instructionCount = it.value.instructions.size
            )
        }
        setState { it.copy(items = items) }
    }
}

data class ProgramListState(
    val items: List<ProgramItem> = emptyList()
)

data class ProgramItem(
    val programId: Int,
    val programName: String,
    val instructionCount: Int
)