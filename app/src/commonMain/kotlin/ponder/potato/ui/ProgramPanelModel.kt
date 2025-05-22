package ponder.potato.ui

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ponder.potato.GameService
import ponder.potato.model.game.ExecutorState
import ponder.potato.model.game.GameState
import ponder.potato.model.game.StateEntity
import ponder.potato.model.game.readWithState
import pondui.ui.core.StateModel

class ProgramPanelModel(
    private val service: GameService = GameService()
): StateModel<ProgramPanelState>(ProgramPanelState()) {

    val game get() = service.game

    private val _entity = MutableStateFlow<StateEntity<ExecutorState>?>(null)
    private var _entityId: Long? = null
    val entity = _entity.asStateFlow()

    // _entityId?.let { game.entities.readWithState<ExecutorState>(it) }
    fun setEntityId(entityId: Long) {
        _entityId = entityId
        refreshEntity()
    }

    fun refreshEntity() {
        _entity.value = _entityId?.let { game.entities.readWithState<ExecutorState>(it) }
        val entity = _entity.value ?: return
        val program = game.programs[entity.state.programId]
        setState { it.copy(
            isVisible = true,
            programName = program?.name,
            programItems = game.programs.toProgramItems()
        ) }
    }

    fun update(state: GameState) {
        refreshEntity()
    }

    fun chooseProgram(programId: Int) {
        val entity = _entity.value ?: return
        entity.state.programId = programId
        refreshEntity()
    }
}

data class ProgramPanelState(
    val isVisible: Boolean = false,
    val programName: String? = null,
    val programItems: List<ProgramItem> = emptyList()
)