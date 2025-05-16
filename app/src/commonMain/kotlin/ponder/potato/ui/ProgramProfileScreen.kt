package ponder.potato.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import ponder.potato.LaunchedGameUpdate
import ponder.potato.ProgramProfileRoute
import pondui.ui.controls.*
import pondui.ui.nav.LocalPortal
import pondui.ui.nav.Scaffold

@Composable
fun ProgramProfileScreen(
    route: ProgramProfileRoute,
    viewModel: ProgramProfileModel = viewModel { ProgramProfileModel(route) }
) {
    val state by viewModel.state.collectAsState()
    val portal = LocalPortal.current

    LaunchedGameUpdate(viewModel::update)

    LaunchedEffect(state.programName) {
        if (state.programName.isNotEmpty()) {
            portal.setTitle(state.programName)
        }
    }

    Scaffold {
        Column {
            for(instruction in state.instructions) {
                InstructionItemView(instruction)
            }
        }
    }
}