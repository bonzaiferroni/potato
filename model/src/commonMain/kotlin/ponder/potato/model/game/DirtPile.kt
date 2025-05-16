package ponder.potato.model.game

import androidx.compose.runtime.State
import kotlinx.serialization.Serializable

class DirtPile(
    override val state: DirtPileState = DirtPileState()
): StateEntity<DirtPileState>() {
    override val components = listOf<StateComponent<*>>()
}

@Serializable
data class DirtPileState(
    override val capacity: Double = 1000.0,
    override var position: MutablePosition = MutablePosition(),
    override var intent: Intent? = null
): EntityStorageState {
    override val resource get() = Resource.Dirt
    override val isAlive get() = true
}