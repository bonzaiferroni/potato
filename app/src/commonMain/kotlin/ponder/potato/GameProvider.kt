package ponder.potato

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ponder.potato.model.game.GameState
import pondui.ui.core.StateModel
import kotlin.time.Duration.Companion.seconds

@Composable
fun ProvideGame(
    content: @Composable () -> Unit
) {
    val gameHost: GameHost = remember { GameHost() }

    CompositionLocalProvider(LocalGame provides gameHost) {
        content()
    }
}

val LocalGame = staticCompositionLocalOf<GameHost> {
    error("no game provided")
}

class GameHost(
    private val service: GameService = GameService()
) : StateModel<GameState>(GameState()) {

    init {
        viewModelScope.launch {
            service.init()
            while (true) {
                delay(1.seconds)
                service.update(1.0)
                setState { service.game.state.copy() }
            }
        }
    }
}

@Composable
fun LaunchedGameUpdate(onUpdate: (GameState) -> Unit) {
    val gameState by LocalGame.current.state.collectAsState()

    LaunchedEffect(gameState) {
        onUpdate(gameState)
    }
}