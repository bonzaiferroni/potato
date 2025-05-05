package ponder.potato.ui

import ponder.potato.GameService
import ponder.potato.model.game.EntityMap
import ponder.potato.model.game.components.AetherStorageState
import ponder.potato.model.game.components.DreamerState
import ponder.potato.model.game.components.readAetherMax
import ponder.potato.model.game.entities.Potato
import ponder.potato.model.game.entities.Shroom
import ponder.potato.model.game.entities.Sprite
import ponder.potato.model.game.read
import ponder.potato.model.game.sumOf
import ponder.potato.model.game.zones.Cave
import ponder.potato.model.game.zones.Dream
import ponder.potato.model.game.zones.GameState
import ponder.potato.model.game.zones.readZone
import pondui.ui.core.StateModel

class DreamScreenModel(
    private val service: GameService = GameService()
) : StateModel<DreamScreenState>(DreamScreenState()) {

    private val game get() = service.game
    private val dream = game.readZone<Dream>()
    private val cave = game.readZone<Cave>()
    private val potato get() = game.entities.read<Potato>()

    fun update(gameState: GameState) {
        refreshState(gameState.delta)
    }

    fun refreshState(delta: Double = 1.0) {
        val resources = game.resources
        val aetherMax = game.entities.readAetherMax()
        val dreamLevel = potato?.state?.level ?: 1
        setState {
            it.copy(
                aether = resources.aether,
                aetherMax = aetherMax,
                level = dreamLevel,
                dreamProgress = dream.state.progress,
                dreamProgressMax = dream.state.resolution,
                levelCost = dream.state.levelCost,
                delta = delta,
                spriteCost = cave.spriteCost,
                spriteCount = cave.spriteCount,
                spriteAether = game.entities.readSpriteAether(dreamLevel),
                maxSpriteCount = cave.maxSpriteCount,
                shroomCost = cave.shroomCost,
                shroomCount = cave.shroomCount,
                maxShroomCount = cave.maxShroomCount,
                shroomStorage = game.entities.readShroomStorage()
            )
        }
    }

    fun resolveDream() {
        dream.resolve()
        refreshState()
    }

    fun dreamSprite() {
        cave.manifestSprite()
        refreshState()
    }

    fun dreamShroom() {
        cave.manifestShroom()
        refreshState()
    }

    fun dreamBard() {
        cave.dreamBard()
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
    val maxSpriteCount: Int = 0,
    val shroomCost: Double = 0.0,
    val shroomCount: Int = 0,
    val maxShroomCount: Int = 0,
    val spriteAether: Double = 0.0,
    val shroomStorage: Double = 0.0,
) {
    val progressRatio get() = minOf(1.0, dreamProgress / dreamProgressMax).toFloat().takeIf { it != Float.NaN } ?: 0f
    val aetherRatio get() = aetherMax.takeIf { it > 0 }?.let { minOf(1.0, aether / aetherMax).toFloat() } ?: 0f
    val levelProgress get() = minOf(1.0, aether / levelCost).toFloat()
}

fun EntityMap.readShroomStorage() = this.sumOf<AetherStorageState>({ it is Shroom }) { it.aetherStorage }
fun EntityMap.readSpriteAether(dreamLevel: Int) = this.sumOf<DreamerState>({ it is Sprite }) { it.getReward(dreamLevel) }