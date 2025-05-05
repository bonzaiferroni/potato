package ponder.potato.ui

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
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
import kabinet.utils.toMetricString
import ponder.potato.LocalGame
import ponder.potato.model.game.zones.Cave
import pondui.ui.controls.Button
import pondui.ui.controls.Card
import pondui.ui.controls.Divider
import pondui.ui.controls.ProgressBar
import pondui.ui.controls.Text
import pondui.ui.controls.actionable
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

    Scaffold {
        Column(
            verticalArrangement = Pond.ruler.columnTight,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Dreaming...")
            ProgressBar(state.progressRatio)
        }
        ZoneView(Cave::class, false)
        FlowRow(
            horizontalArrangement = Arrangement.SpaceBetween,
            itemVerticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dream Level: ${state.level}", modifier = Modifier.weight(1f))
            ProgressBar(state.aetherRatio, modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Aether: ${state.aether.toMetricString()}")
                    Text(state.aetherMax.toMetricString())
                }
            }
        }
        PurchaseBar(
            label = "Sprite",
            cost = state.spriteCost,
            ratio = state.aether / state.spriteCost,
            purchase = viewModel::manifestSprite
        ) {
            Text("Sprites provide extra Aether at the end of each dream.")
            if (state.spriteCount > 0) {
                Text("You have ${state.spriteCount} sprites that provide ${state.spriteAether.toMetricString()} of The Aether at the end of each dream.")
            }
        }
        PurchaseBar(
            label = "Shroom",
            cost = state.shroomCost,
            ratio = state.aether / state.shroomCost,
            purchase = viewModel::manifestShroom
        ) {
            Text("Shrooms let you hold more Aether.")
            if (state.shroomCount > 0) {
                Text("You have ${state.shroomCount} shrooms that hold ${state.shroomStorage.toMetricString()} additional Aether.")
            }
        }
        PurchaseBar(
            label = "Dream resolution",
            cost = state.levelCost,
            ratio = state.aether / state.levelCost,
            buttonLabel = "Resolve",
            purchase = viewModel::dive
        ) {
            Text("Understand the meaning of this dream, to open the way to the next.")
        }
        PurchaseBar(
            label = "Bard",
            cost = 2000.0,
            ratio = 0.0,
            purchase = { }
        ) {
            Text("Dream of a bard. Requires a deeper level of the dream.")
        }
    }
}

@Composable
fun LevelButton(
    levelRatio: Float,
    aether: Double,
    cost: Double,
    levelUp: () -> Unit,
) {
    val animatedRatio by animateFloatAsState(levelRatio)
    val color = when {
        levelRatio >= 1f -> Pond.colors.secondary
        else -> Pond.colors.tertiary
    }

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
                .actionable("Level up", aether >= cost, onClick = levelUp)
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(IntrinsicSize.Max)
            ) {
                AnimatedQuantity(aether)
                Divider(modifier = Modifier.fillMaxWidth())
                AnimatedQuantity(cost)
            }
        }
    }
}