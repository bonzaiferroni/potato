package ponder.potato.model.game.components

import ponder.potato.model.game.EntityMap
import ponder.potato.model.game.entities.EntityState
import ponder.potato.model.game.entities.Shroom
import ponder.potato.model.game.entities.StateEntity
import ponder.potato.model.game.sumOf

interface AetherStorageState: EntityState {
    val aetherStorage: Double
}

class AetherStorage(
    override val entity: StateEntity<AetherStorageState>
): StateComponent<AetherStorageState>() {
}

fun EntityMap.readAetherMax() = this.sumOf<AetherStorageState> { it.aetherStorage }