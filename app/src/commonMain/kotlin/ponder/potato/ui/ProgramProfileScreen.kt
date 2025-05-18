package ponder.potato.ui

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import ponder.potato.LaunchedGameUpdate
import ponder.potato.ProgramProfileRoute
import pondui.ui.controls.*
import pondui.ui.nav.LocalPortal
import pondui.ui.nav.Scaffold
import pondui.ui.theme.Spacing

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
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.instructions.isEmpty()) {
                Text("Instructions...", modifier = Modifier.alpha(.5f))
            }
            for (instruction in state.instructions) {
                InstructionItemView(instruction)
            }
        }
        Column(
            spacing = Spacing.Tight,
            modifier = Modifier.background(Color.Black.copy(.1f))
                .fillMaxWidth()
        ) {
            for (instruction in state.availableInstructions) {
                Button(onClick = { viewModel.addInstruction(instruction.id) }) {
                    InstructionItemView(instruction)
                }
            }
        }
    }
}