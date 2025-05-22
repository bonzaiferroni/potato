package ponder.potato.ui

import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import ponder.potato.model.game.Bard
import ponder.potato.model.game.Bot
import ponder.potato.model.game.Entity
import ponder.potato.model.game.Imp
import ponder.potato.model.game.Potato
import ponder.potato.model.game.Shroom
import ponder.potato.model.game.Sprite
import potato.app.generated.resources.Res

suspend fun Entity?.toLottieResource(isMoving: Boolean) = when (this) {
    is Shroom -> "shroom1"
    is Imp -> "imp1"
    is Sprite -> "sprite1"
    is Bard -> "fox1"
    is Potato -> "potato1"
    is Bot -> "imp"
    else -> "gears"
}.let {
    val filename = "files/${it}_${if (isMoving) "move" else "idle"}.json"
    if (lottieCache.contains(filename)) {
        lottieCache.getValue(filename)
    } else {
        val spec = LottieCompositionSpec.JsonString(Res.readBytes(filename).decodeToString())
        lottieCache[filename] = spec
        spec
    }
}

private val lottieCache = mutableMapOf<String, LottieCompositionSpec>()