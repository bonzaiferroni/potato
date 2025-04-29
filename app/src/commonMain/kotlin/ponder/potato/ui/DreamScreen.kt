package ponder.potato.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import ponder.potato.LocalGame
import pondui.ui.controls.Text
import pondui.ui.nav.Scaffold

@Composable
fun DreamScreen(
    viewModel: DreamScreenModel = viewModel { DreamScreenModel() }
) {
    val state by viewModel.state.collectAsState()
    val gameState by LocalGame.current.state.collectAsState()

    LaunchedEffect(gameState) {
        viewModel.update(gameState)
    }

    Scaffold {
        Text("Aether: ${state.aether}")
    }
}