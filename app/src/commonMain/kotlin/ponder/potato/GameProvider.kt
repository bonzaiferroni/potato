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
    gameModel: GameModel = remember { GameModel() },
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalGame provides gameModel) {
        content()
    }
}

val LocalGame = staticCompositionLocalOf<GameModel> {
    error("no game provided")
}

class GameModel(
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