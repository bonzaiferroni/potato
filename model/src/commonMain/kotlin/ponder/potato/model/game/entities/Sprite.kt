package ponder.potato.model.game.entities

import kotlinx.serialization.Serializable
import ponder.potato.model.game.components.*
import ponder.potato.model.game.*
import ponder.potato.model.game.zones.Dream

class Sprite(
    override val state: SpriteState = SpriteState()
) : StateEntity<SpriteState>() {

    override val components = listOf(
        Meander(this),
        Spirit(this),
        Dreamer(this)
    )
}

@Serializable
data class SpriteState(
    override val level: Int = 1,
    override var spirit: Int = 0,
    override val position: MutablePosition = MutablePosition(),
    override var destination: MutablePosition = MutablePosition(),
    override var oppositionId: Long? = null,
) : SpiritState, ProgressState, MoverState, OpposerState, DreamerState {
    override val maxSpirit get() = factorValue(100, level, 1.2).toInt()
    override val power get() = factorValue(5, level, 1.2).toInt()
    override val aetherReward get() = factorValue(10, level, 1.2)
}