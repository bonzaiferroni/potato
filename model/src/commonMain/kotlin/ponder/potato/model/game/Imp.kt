package ponder.potato.model.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class Imp(
    override val state: ImpState = ImpState()
) : StateEntity<ImpState>() {

    override val components = listOf(
        Spirit(this),
        Hunter(this) { it is Sprite }
    )
}

@Serializable
@SerialName("imp")
data class ImpState(
    override var targetId: Long? = null,
    override var spirit: Int = 0,
    override val level: Int = 1,
    override val position: MutablePosition = MutablePosition(),
    override val speed: Float = 1.2f,
    override var log: String? = null,
    override var intent: Intent? = null,
) : SpiritState, LevelState, OpposerState {
    override val maxSpirit get() = factorValue(100, level, 1.2).toInt()
    override val power get() = factorValue(10, level, 1.2).toInt()
}