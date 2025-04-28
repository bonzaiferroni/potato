package ponder.potato

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import ponder.potato.model.game.zones.GameData
import ponder.potato.model.game.zones.Game
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

@Composable
fun ProvideGame(
    content: @Composable () -> Unit
) {
    val game = Game(GameData())

    LaunchedEffect(Unit) {
        while (true) {
            val now = Clock.System.now()
            delay(1.seconds)
            val delta = Clock.System.now() - now
            game.update(delta.toDouble(DurationUnit.SECONDS))
        }
    }

    CompositionLocalProvider(LocalGame provides game) {
        content()
    }
}

val LocalGame = staticCompositionLocalOf<Game> {
    error("no game provided")
}