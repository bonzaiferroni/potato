package ponder.potato.ui

import ponder.potato.GameService
import ponder.potato.model.game.Program
import pondui.ui.core.StateModel

class ProgramListModel(
    private val gameService: GameService = GameService()
): StateModel<ProgramListState>(ProgramListState()) {

    val game get() = gameService.game
}

data class ProgramListState(
    val programs: List<Program> = emptyList()
)