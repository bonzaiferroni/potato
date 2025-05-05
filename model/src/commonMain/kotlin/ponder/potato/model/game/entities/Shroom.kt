package ponder.potato.model.game.entities

import ponder.potato.model.game.MutablePosition
import ponder.potato.model.game.components.AetherStorage
import ponder.potato.model.game.components.AetherStorageState
import ponder.potato.model.game.factorValue

class Shroom(
    override val state: ShroomState = ShroomState()
) : StateEntity<ShroomState>() {
    override val components = listOf(
        AetherStorage(this)
    )
}

data class ShroomState(
    override val position: MutablePosition = MutablePosition(),
    override var level: Int = 1,
) : EntityState, AetherStorageState, ProgressState {
    override val isAlive: Boolean get() = true
    override val aetherStorage get() = factorValue(300, level, 1.2)
}