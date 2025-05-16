package ponder.potato.ui

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ponder.potato.GameService
import pondui.ui.core.StateModel

class GameDataModel(
    private val service: GameService = GameService()
): StateModel<GameDataState>(GameDataState()) {

    fun save() {
        service.save()
    }

    fun reset() {
        service.reset()
    }
}

data class GameDataState(
    val canLoad: Boolean = false,
    val saveTime: Instant = Clock.System.now()
)