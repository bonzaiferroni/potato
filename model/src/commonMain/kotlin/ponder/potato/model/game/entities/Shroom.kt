package ponder.potato.model.game.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ponder.potato.model.game.MutablePosition
import ponder.potato.model.game.Resource
import ponder.potato.model.game.components.EntityStorage
import ponder.potato.model.game.components.EntityStorageState
import ponder.potato.model.game.components.LevelState
import ponder.potato.model.game.components.NameState
import ponder.potato.model.game.components.Namer
import ponder.potato.model.game.components.VisitorState
import ponder.potato.model.game.factorValue

class Shroom(
    override val state: ShroomState = ShroomState()
) : StateEntity<ShroomState>() {
    override val components = listOf(
        EntityStorage(this),
        Namer(this),
    )
}

@Serializable
@SerialName("shroom")
data class ShroomState(
    override val position: MutablePosition = MutablePosition(),
    override var level: Int = 1,
    override var log: String? = null,
    override var visitorId: Long? = null,
    override var name: String? = null,
    override var intent: Intent? = null,
) : EntityState, EntityStorageState, LevelState, VisitorState, NameState {
    override val isAlive: Boolean get() = true
    override val storedValue get() = factorValue(500, level, 1.2)
    override val storedResource get() = Resource.Aether
}