package ponder.potato.model.game.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ponder.potato.model.game.components.*
import ponder.potato.model.game.*

class Sprite(
    override val state: SpriteState = SpriteState()
) : StateEntity<SpriteState>() {

    override val components = listOf(
        Meander(this),
        Spirit(this),
        Dreamer(this),
        Namer(this),
        Leveler(this),
    )
}

@Serializable
@SerialName("sprite")
data class SpriteState(
    override var level: Int = 1,
    override var progress: Int = 0,
    override var spirit: Int = 0,
    override val position: MutablePosition = MutablePosition(),
    override var destination: MutablePosition = MutablePosition(),
    override var oppositionId: Long? = null,
    override var name: String? = null,
    override var status: String? = null,
) : SpiritState, ProgressState, MoverState, OpposerState, DreamerState, NameState {
    override val maxSpirit get() = factorValue(100, level, 1.2).toInt()
    override val power get() = factorValue(5, level, 1.2).toInt()
    override val aetherReward get() = factorValue(20, level, 1.2)
}