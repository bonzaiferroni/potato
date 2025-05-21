package ponder.potato.ui

import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import pondui.ui.controls.H1

@Composable
fun EntityPanel(
    entityId: Long,
    viewModel: EntityPanelModel = viewModel { EntityPanelModel(entityId) }
) {
    val state by viewModel.state.collectAsState()

    H1(state.entityName)

    ProgramPanel(entityId)
}