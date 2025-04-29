package ponder.potato.model.game.entities

import kotlinx.serialization.Serializable
import ponder.potato.model.game.components.MoverComponent
import ponder.potato.model.game.components.MoverState
import ponder.potato.model.game.components.PositionComponent
import ponder.potato.model.game.components.MutablePosition
import ponder.potato.model.game.components.VitalityComponent
import ponder.potato.model.game.components.VitalityState
import ponder.potato.model.game.factorValue

class Sprite(state: SpriteState = SpriteState()) : StateEntity<SpriteState>(state) {
    init {
        add(PositionComponent(this))
        add(MoverComponent(this))
        add(VitalityComponent(this))
    }
}

@Serializable
data class SpriteState(
    override val level: Int = 1,
    override var health: Int = 0,
    override var isAlive: Boolean = false,
    override val position: MutablePosition = MutablePosition(),
    override val target: MutablePosition = MutablePosition(),
): VitalityState, ProgressState, MoverState {
    override val maxHealth get() = factorValue(100, level, 1.2).toInt()
}