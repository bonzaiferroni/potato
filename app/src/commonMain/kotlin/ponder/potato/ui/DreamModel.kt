package ponder.potato.ui

import ponder.potato.GameService
import ponder.potato.model.game.components.readAetherMax
import ponder.potato.model.game.zones.GameState
import pondui.ui.core.StateModel

class DreamScreenModel(
    private val service: GameService = GameService()
) : StateModel<DreamScreenState>(DreamScreenState()) {

    private val game get() = service.game
    private val dream get() = service.game.map.dream

    fun update(gameState: GameState) {
        refreshState(gameState.delta)
    }

    fun refreshState(delta: Double = 1.0) {
        val resources = game.resources
        val aetherMax = game.entities.readAetherMax()
        setState {
            it.copy(
                aether = resources.aether,
                aetherMax = aetherMax,
                level = dream.state.level,
                dreamProgress = dream.state.progress,
                dreamProgressMax = dream.state.resolution,
                levelCost = dream.state.levelCost,
                delta = delta,
                spriteCost = dream.state.spriteCost,
                spriteCount = dream.state.spriteCount
            )
        }
    }

    fun dive() {
        dream.dive()
        refreshState()
    }

    fun manifestSprite() {
        dream.manifestSprite()
        refreshState()
    }
}

data class DreamScreenState(
    val aether: Double = 0.0,
    val aetherMax: Double = 0.0,
    val level: Int = 0,
    val levelCost: Double = 0.0,
    val dreamProgress: Double = 0.0,
    val dreamProgressMax: Double = 0.0,
    val delta: Double = 1.0,
    val spriteCost: Double = 0.0,
    val spriteCount: Int = 0,
) {
    val progressRatio get() = minOf(1.0, dreamProgress / dreamProgressMax).toFloat().takeIf { it != Float.NaN } ?: 0f
    val aetherRatio get() = aetherMax.takeIf { it > 0 }?.let { minOf(1.0, aether / aetherMax).toFloat() } ?: 0f
    val levelProgress get() = minOf(1.0, aether / levelCost).toFloat()
}