package ponder.potato.model.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    override var visitorId: Long? = null,
    override var name: String? = null,
    override var intent: Intent? = null,
) : EntityState, EntityStorageState, LevelState, VisitorState, NameState {
    override val isAlive: Boolean get() = true
    override val capacity get() = factorValue(500, level, 1.2)
    override val resource get() = Resource.Aether
}