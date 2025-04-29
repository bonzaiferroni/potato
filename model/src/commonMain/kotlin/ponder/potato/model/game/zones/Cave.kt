package ponder.potato.model.game.zones

import kotlinx.serialization.Serializable
import ponder.potato.model.game.LocalPoint

class Cave(
    dream: Dream,
    // state: CaveState,
) : GameZone() {

    val dream = add(dream, LocalPoint(0f, 0f))
}