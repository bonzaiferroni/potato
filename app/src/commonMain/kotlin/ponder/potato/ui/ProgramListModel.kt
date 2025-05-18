package ponder.potato.ui

import ponder.potato.GameService
import ponder.potato.model.game.GameState
import ponder.potato.model.game.Program
import ponder.potato.model.game.add
import ponder.potato.model.game.getNextId
import pondui.ui.core.StateModel

class ProgramListModel(
    private val gameService: GameService = GameService()
): StateModel<ProgramListState>(ProgramListState()) {

    val game get() = gameService.game

    fun toggleIsCreatingItem() {
        setState { it.copy(isCreatingItem = !it.isCreatingItem) }
    }

    fun setNewItemName(value: String) {
        setState { it.copy(newItemName = value) }
    }

    fun createNewItem() {
        if (!stateNow.isValidNewItem) return
        game.programs.add(Program(stateNow.newItemName))
        setState { it.copy(newItemName = "", isCreatingItem = false) }
    }

    fun update(gameState: GameState) {
        val items = game.programs.map {
            ProgramItem(
                programId = it.key,
                programName = it.value.name,
                instructionCount = it.value.statements.size
            )
        }
        setState { it.copy(items = items) }
    }
}

data class ProgramListState(
    val items: List<ProgramItem> = emptyList(),
    val isCreatingItem: Boolean = false,
    val newItemName: String = "",
) {
    val isValidNewItem = newItemName.isNotEmpty()
}

data class ProgramItem(
    val programId: Int,
    val programName: String,
    val instructionCount: Int
)