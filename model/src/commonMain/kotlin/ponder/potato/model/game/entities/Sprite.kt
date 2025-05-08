package ponder.potato.model.game.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ponder.potato.model.game.components.*
import ponder.potato.model.game.*

class Sprite(
    override val state: SpriteState = SpriteState()
) : StateEntity<SpriteState>() {

    override val components = listOf(
        Spirit(this),
        Dreamer(this),
        Namer(this),
        Leveler(this),
        Opposer(this, 5f) { it is Imp && it.target is Sprite && it.hasIntent(Intent.Oppose) },
        Rester(this) { it is Shroom && it.state.visitorId == null },
        Meander(this),
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
    override var targetId: Long? = null,
    override var name: String? = null,
    override var log: String? = null,
    override var intent: Intent? = null,
) : SpiritState, ProgressState, MoverState, OpposerState, DreamerState, NameState, TargetState {
    override val maxSpirit get() = factorValue(100, level, 1.2).toInt()
    override val power get() = factorValue(5, level, 1.2).toInt()
    override val aetherReward get() = factorValue(20, level, 1.2)
}