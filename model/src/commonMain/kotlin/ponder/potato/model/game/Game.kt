package ponder.potato.model.game

import kotlinx.serialization.Serializable

class Game(data: GameData) : StateEngine<GameState>(data.game) {

    val dream = add(Dream(data.dream))

    override fun update(delta: Double) {
        super.update(delta)
        setState {
            it.copy(
                time = stateNow.time + delta,
                tick = stateNow.tick + 1
            )
        }
    }
}

@Serializable
data class GameState(
    val tick: Long = 0L,
    val time: Double = 0.0,
)

@Serializable
data class GameData(
    val game: GameState,
    val dream: DreamState,
)