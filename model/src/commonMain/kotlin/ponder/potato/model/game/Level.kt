package ponder.potato.model.game

interface LevelState: EntityState {
    val level: Int
}

interface LevelProgressState: LevelState {
    var levelProgress: Int
    override var level: Int

    fun addExperience(experience: Double) {
        val progress = experienceToLevelProgress(level, experience).toInt()
        this.levelProgress += progress
    }
}

class Leveler(
    override val entity: StateEntity<LevelProgressState>
): StateComponent<LevelProgressState>() {

    override fun update(delta: Double) {
        super.update(delta)
        if (state.levelProgress >= 100) {
            state.levelProgress -= 100
            state.level++
            if (entity.isObserved) entity.showEffect(LevelUp(state.level))
        }
    }
}

fun experienceToLevelProgress(level: Int, experience: Double) = (experience / factorValue(100, level, 1.2)) * experience