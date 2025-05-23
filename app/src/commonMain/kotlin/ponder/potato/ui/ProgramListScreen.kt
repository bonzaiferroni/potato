package ponder.potato.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import ponder.potato.LaunchedGameUpdate
import ponder.potato.ProgramProfileRoute
import pondui.ui.controls.Button
import pondui.ui.controls.Cloud
import pondui.ui.controls.ControlRowButton
import pondui.ui.controls.Controls
import pondui.ui.controls.RouteButton
import pondui.ui.controls.Text
import pondui.ui.controls.TextField
import pondui.ui.nav.LazyScaffold
import pondui.ui.theme.Pond

@Composable
fun ProgramListScreen(
    viewModel: ProgramListModel = viewModel { ProgramListModel() }
) {
    val state by viewModel.state.collectAsState()

    LaunchedGameUpdate(viewModel::update)

    Cloud(state.isCreatingItem, viewModel::toggleIsCreatingItem) {
        Controls {
            TextField(state.newItemName, viewModel::setNewItemName, placeholder = "program name")
            ControlRowButton("Create", onClick = viewModel::createNewItem)
        }
    }

    LazyScaffold {
        items(state.items) { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Pond.ruler.rowTight,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(item.programName)
                Spacer(modifier = Modifier.weight(1f))
                RouteButton("Edit") { ProgramProfileRoute(item.programId) }
            }
        }
        item {
            Button("+", onClick = viewModel::toggleIsCreatingItem)
        }
    }
}