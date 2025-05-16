package ponder.potato.model.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class Bard(
    override val state: BardState = BardState()
): StateEntity<BardState>() {
    override val components: List<StateComponent<*>> = listOf(
        Miner(this),
        Mover(this),
    )

    fun startDigging(): EntityAction? {
        if (state.targetId != null) return null
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
data class BardState(
    override val isAlive: Boolean = true,
    override val position: MutablePosition = MutablePosition(),
    override val destination: MutablePosition = MutablePosition(),
    override var intent: Intent? = null,
    override var targetId: Long? = null,
    override var progress: Float? = null,
): EntityState, MoverState, MinerState {
    override val speed get() = 2f
}