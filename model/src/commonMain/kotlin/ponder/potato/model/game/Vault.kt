package ponder.potato.model.game

import kotlinx.serialization.Serializable

class Vault(
    override val state: VaultState = VaultState()
): StateEntity<VaultState>() {
    override val components = listOf<StateComponent<*>>()
}

@Serializable
data class VaultState(
    override var storedValue: Double = 1000.0,
    override val position: MutablePosition = MutablePosition(),
    override var intent: Intent? = null,
): EntityStorageState {
    override val isAlive get() = true
    override val storedResource get() = Resource.Gold
}