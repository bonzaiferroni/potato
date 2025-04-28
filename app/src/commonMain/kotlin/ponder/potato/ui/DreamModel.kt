package ponder.potato.ui

import ponder.potato.model.game.zones.Dream
import ponder.potato.model.game.zones.DreamState
import pondui.ui.core.StateModel

class DreamScreenModel(
    dream: Dream
): StateModel<DreamScreenState>(DreamScreenState(dream.state.value)) {

}

data class DreamScreenState(
    val dreamState: DreamState
)