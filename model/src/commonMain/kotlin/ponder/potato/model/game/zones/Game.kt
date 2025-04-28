package ponder.potato.model.game.zones

import kotlinx.serialization.Serializable
import ponder.potato.model.game.entities.BroomState
import ponder.potato.model.game.entities.StateEntity
import ponder.potato.model.game.entities.EntityState

class Game(data: GameData) : StateZone<GameState>(data.game) {

    val entities = mutableListOf<StateEntity<*>>()

    val dream = add(Dream(data.dream))

    fun <S: EntityState> add(states: List<S>, toEntity: (S) -> StateEntity<S>) =
        states.map { toEntity(it).also { entity -> entities.add(entity) } }

    override fun update(delta: Double) {
        state.time += delta
        state.tick++
        super.update(delta)
        for (entity in entities) {
            if (!entity.state.isActive) continue
            entity.update(delta)
        }
    }
}

@Serializable
data class GameState(
    var tick: Long = 0L,
    var time: Double = 0.0,
)

@Serializable
data class GameData(
    val game: GameState = GameState(),
    val dream: DreamState = DreamState(),
    val brooms: List<BroomState> = emptyList()
)