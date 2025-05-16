package ponder.potato.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import pondui.ui.controls.Button
import pondui.ui.nav.Scaffold

@Composable
fun GameDataScreen(
    viewModel: GameDataModel = viewModel { GameDataModel() }
) {
    Scaffold {
        Button("Save", onClick = viewModel::save)
        Button("Reset", onClick = viewModel::reset)
    }
}