package ponder.potato.model.game.components

import ponder.potato.model.game.LevelUp
import ponder.potato.model.game.entities.EntityState
import ponder.potato.model.game.entities.StateEntity
import ponder.potato.model.game.factorValue

interface LevelState: EntityState {
    val level: Int
}

interface ProgressState: LevelState {
    var progress: Int
    override var level: Int

    fun addExperience(experience: Double) {
        val progress = experienceToProgress(level, experience).toInt()
        this.progress += progress
    }
}

class Leveler(
    override val entity: StateEntity<ProgressState>
): StateComponent<ProgressState>() {

    override fun update(delta: Double) {
        super.update(delta)
        if (state.progress >= 100) {
            state.progress -= 100
            state.level++
            entity.showEffect { LevelUp(state.level) }
            entity.state.status = "Reached a new level: ${state.level}"
        }
    }
}

fun experienceToProgress(level: Int, experience: Double) = (experience / factorValue(100, level, 1.2)) * experience