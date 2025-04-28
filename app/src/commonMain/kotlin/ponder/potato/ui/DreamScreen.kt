package ponder.potato.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import ponder.potato.LocalGame

@Composable
fun DreamScreen(
    viewModel: DreamScreenModel = viewModel { DreamScreenModel(LocalGame.current.dream) }
) {
}