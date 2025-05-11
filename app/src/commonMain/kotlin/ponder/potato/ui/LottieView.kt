package ponder.potato.ui

import androidx.compose.runtime.Composable
import io.github.alexzhirkevich.compottie.DotLottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import ponder.potato.model.game.entities.Bard
import ponder.potato.model.game.entities.Entity
import ponder.potato.model.game.entities.Imp
import ponder.potato.model.game.entities.Potato
import ponder.potato.model.game.entities.Shroom
import ponder.potato.model.game.entities.Sprite
import potato.app.generated.resources.Res

suspend fun Entity?.toLottieResource(isMoving: Boolean) = when (this) {
    is Shroom -> "shroom1"
    is Imp -> "imp1"
    is Sprite -> "sprite1"
    is Bard -> "fox1"
    is Potato -> "potato1"
    else -> "gears"
} .let { LottieCompositionSpec.JsonString(Res.readBytes("files/${it}_${if (isMoving) "move" else "idle"}.json").decodeToString()) }