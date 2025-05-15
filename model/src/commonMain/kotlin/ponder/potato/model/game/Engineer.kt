package ponder.potato.model.game

import kotlinx.serialization.Serializable

class Engineer(
    override val state: EngineerState = EngineerState()
): StateEntity<EngineerState>() {
    override val components: List<StateComponent<*>> = listOf()
}

@Serializable
data class EngineerState(
    override var intent: Intent? = null,
    override val position: MutablePosition = MutablePosition()
): EntityState {
    override val isAlive get() = true
}