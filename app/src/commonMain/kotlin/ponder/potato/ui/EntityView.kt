package ponder.potato.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stevdza_san.sprite.domain.rememberSpriteState
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kabinet.utils.random
import kabinet.utils.toMetricString
import ponder.potato.LaunchedGameUpdate
import ponder.potato.model.game.*
import ponder.potato.model.game.Imp
import pondui.ui.controls.ProgressBar
import pondui.ui.controls.Text
import pondui.utils.lighten

@Composable
fun ZoneScope.EntityView(
    entityId: Long,
    fullVisibility: Boolean,
    viewModel: EntityViewModel = viewModel(key = entityId.toString()) { EntityViewModel(entityId) }
) {
    val state by viewModel.state.collectAsState()

    LaunchedGameUpdate(viewModel::update)

    val spriteState = rememberSpriteState(
        totalFrames = 9,
        framesPerRow = 3,
        animationSpeed = 50
    )

    LaunchedEffect(Unit) {
        spriteState.start()
    }

    LaunchedEffect(state.isMoving) {
        if (state.isMoving) {
            spriteState.start()
        } else {
            spriteState.stop()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.dispose()
            spriteState.stop()
            spriteState.cleanup()
        }
    }

    val animatedSpirit by animateFloatAsState(state.spiritRatio)

    ZoneObject(state.x, state.y, state.delta) {
        for (o in viewModel.effects) {
            val (text, color) = getText(o.effect) ?: continue
            key(o.key) {

                val effectTime = remember(o.key) { Animatable(0f) }
                val effectX = remember { Float.random(1f, 5f) }
                val effectY = remember { Float.random(-10f, 10f) }
                val offset = 60f
                val wind = if (viewModel.entity is Imp) 1f else -1f

                LaunchedEffect(Unit) {
                    effectTime.animateTo(
                        targetValue = EFFECT_DISPLAY_SECONDS.toFloat(),
                        animationSpec = tween(durationMillis = 1000 * EFFECT_DISPLAY_SECONDS, easing = LinearEasing)
                    )
                }

                Text(
                    text = text,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    color = color,
                    modifier = Modifier.graphicsLayer {
                        translationY = -(effectTime.value * 13f + offset) + effectY
                        translationX = (effectTime.value * effectTime.value * effectX * wind)
                        alpha = minOf(1f, EFFECT_DISPLAY_SECONDS - effectTime.value)
                    }
                )
            }
        }

        if (!fullVisibility) return@ZoneObject

        var isMoving by remember { mutableStateOf(state.isMoving) }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val composition by rememberLottieComposition(isMoving) { viewModel.entity.toLottieResource(state.isMoving) }
            val progress by animateLottieCompositionAsState(
                composition,
                iterations = Compottie.IterateForever,
            )
            LaunchedEffect(progress) {
                if (!isMoving || progress > .99f) {
                    isMoving = state.isMoving
                }
            }
            // Text("(${state.x}, ${state.y})", style = TextStyle(fontSize = 8.sp))
            state.progress?.let {
                ProgressBar(it)
            }
            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(2.dp)
                    .drawBehind {
                        if (animatedSpirit == 1f) return@drawBehind
                        drawRect(
                            color = Color.Red
                        )
                        drawRect(
                            color = Color.Green,
                            size = Size(size.width * animatedSpirit, size.height)
                        )
                    }
            )
            Image(
                painter = rememberLottiePainter(
                    composition = composition,
                    iterations = Compottie.IterateForever,
                ),
                contentDescription = "Lottie animation"
            )
        }
    }
}

fun getText(effect: Effect) = when {
    effect is Despirit -> effect.spirit.toString() to Color.Red.lighten(.5f)
    effect is ResourceReward -> effect.amount.toMetricString() to Resource.Aether.toColor().lighten(.5f)
    effect is LevelUp -> effect.level.toString() to Color.Blue.lighten(.5f)
    effect is ExperienceUp -> effect.experience.toMetricString() to Color.Yellow.lighten(.5f)
    effect is Inspirit -> effect.spirit.toString() to Color.Green.lighten(.5f)
    else -> null
}

@Composable
fun animatePosition(
    value: Float,
    delta: Double,
    snap: Boolean = false
) = animateFloatAsState(
    targetValue = value,
    animationSpec = if (snap) snap() else tween((delta * 1000).toInt(), easing = LinearEasing)
)