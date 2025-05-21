package ponder.potato.ui

import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import ponder.potato.LaunchedGameUpdate
import pondui.ui.controls.Button
import pondui.ui.controls.FlowRow
import pondui.ui.controls.H1
import pondui.ui.theme.Spacing

@Composable
fun ProgramPanel(
    entityId: Long,
) {
    val viewModel = viewModel (key = entityId.toString()) { ProgramPanelModel(entityId) }
    val state by viewModel.state.collectAsState()
    LaunchedGameUpdate(viewModel::update)

    if (!state.isVisible) return

    val programName = state.programName
    if (programName != null) {
        H1(programName)
    } else {
        FlowRow(spacing = Spacing.Tight, modifier = Modifier.background(Color.Black.copy(.1f))) {
            for (item in state.programItems) {
                Button(item.programName, onClick = { viewModel.chooseProgram(item.programId)})
            }
        }
    }
}