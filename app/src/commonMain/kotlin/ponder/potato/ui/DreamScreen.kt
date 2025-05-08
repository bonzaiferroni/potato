package ponder.potato.ui

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kabinet.utils.toMetricString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringArrayResource
import ponder.potato.LaunchedGameUpdate
import ponder.potato.ResourceColor
import ponder.potato.model.game.zones.BARD_COST
import ponder.potato.model.game.zones.Cave
import pondui.ui.controls.*
import pondui.ui.nav.BottomBarSpacer
import pondui.ui.nav.TopBarSpacer
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideBookColors
import potato.app.generated.resources.Res
import potato.app.generated.resources.bardfox_card_full
import potato.app.generated.resources.dream_card_full
import potato.app.generated.resources.dream_description
import potato.app.generated.resources.imp_card_full
import potato.app.generated.resources.potato_card_full
import potato.app.generated.resources.shroom_card_full
import potato.app.generated.resources.sprite_card_full

@Composable
fun DreamScreen(
    viewModel: DreamModel = viewModel { DreamModel() }
) {
    val state by viewModel.state.collectAsState()

    LaunchedGameUpdate(viewModel::update)

    TopBarSpacer()

    ZoneView(Cave::class)
    Tabs {
        Tab("Area") {
            Card(
                innerPadding = 0.dp,
            ) {
                Row {
                    Image(
                        painter = painterResource(Res.drawable.dream_card_full),
                        contentDescription = "Image of purchase",
                        modifier = Modifier.width(200.dp)
                    )
                    ProvideBookColors {
                        Column(
                            verticalArrangement = Pond.ruler.columnTight,
                            modifier = Modifier.fillMaxWidth()
                                .background(Pond.localColors.surface)
                                .padding(Pond.ruler.innerPadding)
                        ) {
                            H4("The Dream", color = Pond.colors.tertiary)
                            for (text in stringArrayResource(Res.array.dream_description)) {
                                Text(text)
                            }
                        }
                    }
                }
            }
        }
        Tab("Shape Dream") {
            FlowRow(
                horizontalArrangement = Arrangement.SpaceBetween,
                itemVerticalAlignment = Alignment.CenterVertically
            ) {
                Text("Dream Level: ${state.level}", modifier = Modifier.weight(1f))
                ProgressBar(state.aetherRatio, color = ResourceColor.aether, modifier = Modifier.weight(1f)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Aether: ${state.aether.toMetricString()}")
                        Text(state.aetherMax.toMetricString())
                    }
                }
                Row(modifier = Modifier.weight(1f)) {
                    Text("Dreaming... ")
                    ProgressBar(state.progressRatio, color = ResourceColor.aether)
                }
            }
            PurchaseBar(
                label = "Sprite",
                resource = Res.drawable.sprite_card_full,
                cost = state.spriteCost,
                ratio = if (state.spriteCount < state.maxSpriteCount) {
                    state.aether / state.spriteCost
                } else null,
                currentCount = state.spriteCount,
                maxCount = state.maxSpriteCount,
                resourceName = "Aether",
                resourceColor = ResourceColor.aether,
                purchase = viewModel::dreamSprite,
            ) {

                Text("Sprites provide extra Aether at the end of each dream.")
                if (state.spriteCount > 0) {
                    Text("This dream has ${state.spriteCount} out of ${state.maxSpriteCount} possible sprites that provide ${state.spriteAether.toMetricString()} of The Aether at the end of each dream.")
                }
            }
            PurchaseBar(
                label = "Shroom",
                resource = Res.drawable.shroom_card_full,
                cost = state.shroomCost,
                ratio = if (state.shroomCount < state.maxShroomCount) {
                    state.aether / state.shroomCost
                } else null,
                currentCount = state.shroomCount,
                maxCount = state.maxShroomCount,
                purchase = viewModel::dreamShroom
            ) {
                Text("Shrooms let you hold more Aether.")
                if (state.shroomCount > 0) {
                    Text("You have ${state.shroomCount} shrooms that hold ${state.shroomStorage.toMetricString()} additional Aether.")
                }
            }
            PurchaseBar(
                label = "Dream resolution",
                resource = Res.drawable.potato_card_full,
                cost = state.levelCost,
                ratio = state.aether / state.levelCost,
                buttonLabel = "Resolve",
                currentCount = state.level,
                purchase = viewModel::resolveDream
            ) {
                Text("Find an understanding of the dream, to open the way to the next level.")
            }
            val canPurchaseBard = state.level >= 2
            PurchaseBar(
                label = "Bard",
                resource = Res.drawable.bardfox_card_full,
                cost = BARD_COST,
                ratio = if (canPurchaseBard) state.aether / BARD_COST else null,
                purchase = viewModel::dreamBard
            ) {
                Text("Dream of a bard.")
                if (!canPurchaseBard) {
                    Text("Requires a deeper level of the dream.")
                }
            }
            PurchaseBar(
                label = "Imp",
                resource = Res.drawable.imp_card_full,
            ) {
//                Text("Dream of a bard.")
//                if (!canPurchaseBard) {
//                    Text("Requires a deeper level of the dream.")
//                }
            }
            BottomBarSpacer()
        }
        Tab("Entities", scrollable = false) {
            EntityListView(viewModel.caveId)
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