package ponder.potato

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import ponder.potato.model.game.GameData
import ponder.potato.model.game.generateGame
import ponder.potato.model.game.zones.Cave
import ponder.potato.model.game.zones.GameState
import pondui.ui.core.StateModel
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

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

class GameModel: StateModel<GameState>(GameState()) {

    val data = GameData()
    val engine = generateGame(data)

    init {
        viewModelScope.launch {
            while (true) {
                delay(1.seconds)
                engine.update(1.0)
                setState { engine.state.copy() }
            }
        }
    }
}