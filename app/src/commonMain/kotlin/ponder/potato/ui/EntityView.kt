package ponder.potato.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stevdza_san.sprite.component.SpriteView
import com.stevdza_san.sprite.domain.SpriteSheet
import com.stevdza_san.sprite.domain.SpriteSpec
import com.stevdza_san.sprite.domain.rememberSpriteState
import com.stevdza_san.sprite.util.getScreenWidth
import compose.icons.TablerIcons
import compose.icons.tablericons.Ghost
import compose.icons.tablericons.Man
import compose.icons.tablericons.QuestionMark
import io.ktor.client.plugins.observer.ResponseObserver
import ponder.potato.LocalGame
import ponder.potato.model.game.*
import ponder.potato.model.game.entities.Imp
import ponder.potato.model.game.entities.Sprite
import pondui.ui.controls.Icon
import pondui.ui.controls.Text
import pondui.ui.theme.Pond
import potato.app.generated.resources.Res
import potato.app.generated.resources.fairy_40
import potato.app.generated.resources.fairy_52
import potato.app.generated.resources.fairy_tiny
import potato.app.generated.resources.imp_30
import potato.app.generated.resources.imp_40
import potato.app.generated.resources.imp_52
import potato.app.generated.resources.sprite_small
import potato.app.generated.resources.sprite_tiny

@Composable
fun EntityView(
    entityId: Long,
    boxSize: IntSize,
    viewModel: EntityViewModel = viewModel(key = entityId.toString()) { EntityViewModel(entityId) }
) {
    val state by viewModel.state.collectAsState()
    val gameState by LocalGame.current.state.collectAsState()

    val screenWidth = getScreenWidth()
    val spriteState = rememberSpriteState(
        totalFrames = 9,
        framesPerRow = 3,
        animationSpeed = 50
    )

    LaunchedEffect(Unit) {
        spriteState.start()
    }

    LaunchedEffect(gameState) {
        viewModel.update(gameState)
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

    if (!state.isVisible || boxSize == IntSize.Zero) return

    val animatedX by animatePosition(state.x, state.delta, false)
    val animatedY by animatePosition(state.y, state.delta, false)
    val animatedSpirit by animateFloatAsState(state.spiritRatio)

    val (image, imageSize) = getImage(viewModel.type)
    val centerX = boxSize.width * (animatedX + BOUNDARY_X) / (BOUNDARY_X * 2)
    val centerY = boxSize.height * (animatedY + BOUNDARY_Y) / (BOUNDARY_Y * 2)
    val radius = imageSize / 2
    val radiusPx = with(LocalDensity.current) { radius.dp.toPx() }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width((radius * 2).dp)
            .graphicsLayer {
                translationX = centerX - radiusPx
                translationY = centerY - radiusPx
            }
    ) {
        for (o in viewModel.effects) {
            val text = getText(o.effect) ?: continue
            key(o.key) {

                val effectTime = remember (o.key) { Animatable(0f) }
                val offset = 15f

                LaunchedEffect(Unit) {
                    effectTime.animateTo(
                        targetValue = EFFECT_DISPLAY_SECONDS.toFloat(),
                        animationSpec = tween(durationMillis = 1000 * EFFECT_DISPLAY_SECONDS, easing = LinearEasing)
                    )
                }

                Text(
                    text = text,
                    style = TextStyle(fontSize = Pond.typo.label.fontSize),
                    modifier = Modifier.graphicsLayer {
                        translationY = -(effectTime.value * 13f + offset)
                        alpha = minOf(1f, EFFECT_DISPLAY_SECONDS - effectTime.value)
                    }
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            SpriteView(
                spriteState = spriteState,
                spriteSpec = SpriteSpec(
                    screenWidth = screenWidth.value,
                    default = SpriteSheet(
                        frameWidth = imageSize,
                        frameHeight = imageSize,
                        image = image
                    )
                ),
                modifier = Modifier.fillMaxWidth()
                    .aspectRatio(1f)
                    .graphicsLayer {
                        scaleX = if (state.facingRight) 1f else -1f
                    }
            )
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
        }
    }
}

fun getImage(type: String) = when (type) {
    Sprite::class.simpleName -> Res.drawable.fairy_tiny to 20
    Imp::class.simpleName -> Res.drawable.imp_30 to 30
    else -> Res.drawable.imp_40 to 40
}

fun getText(effect: Effect) = when {
    effect is Despirit -> effect.spirit.toString()
    else -> null
}

@Composable
fun animatePosition(
    value: Float,
    delta: Double,
    snap: Boolean
) = animateFloatAsState(
    targetValue = value,
    animationSpec = if (snap) snap() else tween((delta * 1000).toInt(), easing = LinearEasing)
)