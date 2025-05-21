package ponder.potato.ui

import ponder.potato.GameService
import ponder.potato.model.game.Executor
import ponder.potato.model.game.ExecutorState
import ponder.potato.model.game.GameState
import ponder.potato.model.game.StateEntity
import ponder.potato.model.game.castIfState
import ponder.potato.model.game.read
import ponder.potato.model.game.readAsStateEntity
import pondui.ui.core.StateModel

class ProgramPanelModel(
    private val entityId: Long,
    private val service: GameService = GameService()
): StateModel<ProgramPanelState>(ProgramPanelState()) {

    val game get() = service.game

    fun refreshEntity() {
        val entity = game.entities.readAsStateEntity<ExecutorState>(entityId)
        if (entity != null) {
            val program = game.programs[entity.state.programId]
            setState { it.copy(
                isVisible = true,
                programName = program?.name,
                programItems = game.programs.toProgramItems()
            ) }
        } else {
            setState { it.copy(
                isVisible = false
            )}
        }

    }

    fun update(state: GameState) {
        refreshEntity()
    }

    fun chooseProgram(programId: Int) {
        val entity = game.entities.readAsStateEntity<ExecutorState>(entityId) ?: return
        entity.state.programId = programId
        refreshEntity()
    }
}

data class ProgramPanelState(
    val isVisible: Boolean = false,
    val programName: String? = null,
    val programItems: List<ProgramItem> = emptyList()
)