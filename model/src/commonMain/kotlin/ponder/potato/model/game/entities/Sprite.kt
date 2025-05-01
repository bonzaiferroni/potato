package ponder.potato.model.game.entities

import kotlinx.serialization.Serializable
import ponder.potato.model.game.components.*
import ponder.potato.model.game.*

class Sprite(
    override val state: SpriteState = SpriteState()
) : StateEntity<SpriteState>() {

    override val components = listOf(
        Meander(this),
        Spirit(this),
    )
}

@Serializable
data class SpriteState(
    override val level: Int = 1,
    override var spirit: Int = 0,
    override val position: MutablePosition = MutablePosition(),
    override var destination: MutablePosition = MutablePosition(),
) : SpiritState, ProgressState, MoverState {
    override val maxSpirit get() = factorValue(100, level, 1.2).toInt()
}