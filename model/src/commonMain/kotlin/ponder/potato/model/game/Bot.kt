package ponder.potato.model.game

import kotlinx.serialization.Serializable

class Bot(
    override val state: BotState = BotState()
): StateEntity<BotState>() {

    override val components: List<StateComponent<*>> get() = listOf(
        Miner(this),
        Executor(this),
    )


}

@Serializable
data class BotState(
    override val position: MutablePosition = MutablePosition(),
    override var intent: Intent? = null,
    override var programId: Int? = null,
    override var instructionId: Int? = null,
    override var resource: Resource? = null,
    override var stored: Double = 0.0,
    override var progress: Float? = null,
    override var targetId: Long? = null,
): EntityState, ResourceInventory, MinerState, ExecutorState {
    override val isAlive get() = true
    override val capacity get() = 100.0
}
