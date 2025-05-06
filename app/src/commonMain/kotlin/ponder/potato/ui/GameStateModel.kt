package ponder.potato.ui

import ponder.potato.GameService
import pondui.ui.core.StateModel

open class GameStateModel<T>(
    state: T,
    protected val service: GameService = GameService()
): StateModel<T>(state) {

    protected val game get() = service.game
}