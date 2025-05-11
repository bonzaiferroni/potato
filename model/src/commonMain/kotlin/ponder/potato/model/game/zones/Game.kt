package ponder.potato.model.game.zones

import ponder.potato.model.game.GameData
import ponder.potato.model.game.GameResources
import ponder.potato.model.game.Resource
import ponder.potato.model.game.Resources
import ponder.potato.model.game.components.DreamerState
import ponder.potato.model.game.components.StorageState
import ponder.potato.model.game.entities.Entity
import ponder.potato.model.game.entities.Potato
import ponder.potato.model.game.entities.Shroom
import ponder.potato.model.game.entities.Sprite
import ponder.potato.model.game.read
import ponder.potato.model.game.sumOf

interface Game {
    val state: GameState
    val zones: List<GameZone>
    val entities: Map<Long, Entity>
    val resources: Resources
    val namingWay: NamingWay

    val potato get() = entities.read<Potato>()

    fun toGameData() = GameData(
        dream = zones.firstNotNullOf { it as? Dream }.state,
        game = state,
        resources = (resources as GameResources),
        entityStates = entities.map { it.key to it.value.state }.toMap()
    )

    fun getZone(zoneId: Int) = zones.first { it.id == zoneId }

    val dreamLevel get() = potato?.state?.level ?: 1
}

fun Game.readShroomStorage() =
    entities.sumOf<StorageState>({ it is Shroom }) { it.storage }
fun Game.readSpriteAether() =
    entities.sumOf<DreamerState>({ it is Sprite }) { it.getReward(dreamLevel) }
fun Game.readResourceMax(resource: Resource) =
    entities.sumOf<StorageState> { if (it.isStorageType(resource)) it.storage else 0.0 }
fun Game.readResourceQuantity(resource: Resource) = resources.readQuantity(resource)
fun Game.readResourceStatus(resource: Resource) =
    ResourceStatus(this.readResourceQuantity(resource), this.readResourceMax(resource), resource)