package ponder.potato.ui

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ponder.potato.LocalGame
import pondui.ui.controls.ProgressBar
import pondui.ui.controls.Text
import pondui.ui.nav.Scaffold
import pondui.ui.theme.Pond

@Composable
fun DreamScreen(
    viewModel: DreamScreenModel = viewModel { DreamScreenModel() }
) {
    val state by viewModel.state.collectAsState()
    val gameState by LocalGame.current.state.collectAsState()

    LaunchedEffect(gameState) {
        viewModel.update(gameState)
    }

    LaunchedEffect(state.levelRatio) {
        println(state.levelRatio)
    }

    Scaffold {
        Text("Aether: ${state.aether}")
        ProgressBar(state.progressRatio)
        LevelButton(state.levelRatio)
    }
}

@Composable
fun LevelButton(levelRatio: Float) {
    val animatedRatio by animateFloatAsState(levelRatio)
    val color = Pond.colors.secondary
    val animatedColor = remember { Animatable(color) }
    var previousRatio by remember { mutableStateOf(levelRatio) }
    LaunchedEffect(levelRatio) {
        if (levelRatio > previousRatio) {
            animatedColor.snapTo(Color.White)
            animatedColor.animateTo(color)
        }
        previousRatio = levelRatio
    }


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
            .aspectRatio(2f)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxHeight()
                .aspectRatio(1f)
                .clip(Pond.ruler.round)
                .border(1.dp, color, Pond.ruler.round)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .drawBehind {
                        val diameter = size.minDimension * animatedRatio
                        val radius = diameter / 2f
                        drawCircle(
                            color = animatedColor.value, // or any color ye fancy
                            radius = radius,
                            center = size.center
                        )
                    }
            )
        }
    }
}