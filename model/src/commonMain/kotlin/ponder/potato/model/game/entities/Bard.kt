package ponder.potato.model.game.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ponder.potato.model.game.MutablePosition
import ponder.potato.model.game.components.Miner
import ponder.potato.model.game.components.Mover
import ponder.potato.model.game.components.MoverState
import ponder.potato.model.game.components.StateComponent
import ponder.potato.model.game.components.TargetState
import ponder.potato.model.game.zones.EntityAbility
import ponder.potato.model.game.zones.EntityAction

class Bard(
    override val state: BardState = BardState()
): StateEntity<BardState>() {
    override val components: List<StateComponent<*>> = listOf(
        Miner(this),
        Mover(this),
    )

    fun startDigging(): EntityAction? {
        val target = zone.readFirstOrNull<MinerTargetState>() ?: return null
        return EntityAction(
            entityId = id,
            ability = EntityAbility.Mine,
            label = "Start mining ${target.name}"
        ) {
            state.targetId = target.id
            state.intent = Intent.Mine
        }
    }

    fun stopDigging(): EntityAction? {
        val currentTarget = readTarget<MinerTargetState>() ?: return null
        return EntityAction(
            entityId = id,
            ability = EntityAbility.Mine,
            label = "Stop mining ${currentTarget.name}"
        ) {
            state.targetId = null
            state.intent = null
        }
    }

    override val abilities = listOf(
        ::startDigging,
        ::stopDigging
    )
}

@Serializable
@SerialName("bard")
data class BardState(
    override val isAlive: Boolean = true,
    override val position: MutablePosition = MutablePosition(),
    override var log: String? = null,
    override var intent: Intent? = null,
    override var targetId: Long? = null,
    override val destination: MutablePosition = MutablePosition(),
): EntityState, TargetState, MoverState