package ponder.potato.model.game.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ponder.potato.model.game.MutablePosition
import ponder.potato.model.game.components.AetherStorage
import ponder.potato.model.game.components.AetherStorageState
import ponder.potato.model.game.components.LevelState
import ponder.potato.model.game.factorValue

class Shroom(
    override val state: ShroomState = ShroomState()
) : StateEntity<ShroomState>() {
    override val components = listOf(
        AetherStorage(this)
    )
}

@Serializable
@SerialName("shroom")
data class ShroomState(
    override val position: MutablePosition = MutablePosition(),
    override var level: Int = 1,
    override var status: String? = null,
) : EntityState, AetherStorageState, LevelState {
    override val isAlive: Boolean get() = true
    override val aetherStorage get() = factorValue(500, level, 1.2)
}