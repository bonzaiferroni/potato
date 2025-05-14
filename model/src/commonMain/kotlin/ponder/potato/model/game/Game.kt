package ponder.potato.model.game

interface Game {
    val state: GameState
    val zones: List<GameZone>
    val entities: Map<Long, Entity>
    val storage: Storage
    val namingWay: NamingWay
    val console: GameConsole

    val potato get() = entities.read<Potato>()

    fun toGameData() = GameData(
        dream = zones.firstNotNullOf { it as? Dream }.state,
        game = state,
        resources = (storage as GameStorage),
        entityStates = entities.map { it.key to it.value.state }.toMap()
    )

    fun getZone(zoneId: Int) = zones.first { it.id == zoneId }

    val dreamLevel get() = potato?.state?.level ?: 1
}

fun Game.readShroomStorage() =
    entities.sumOf<EntityStorageState>({ it is Shroom }) { it.storedValue }
fun Game.readSpriteAether() =
    entities.sumOf<DreamerState>({ it is Sprite }) { it.getReward(dreamLevel) }
fun Game.readResourceMax(resource: Resource) =
    entities.sumOf<EntityStorageState> { if (it.isStorageType(resource)) it.storedValue else 0.0 }
fun Game.readResourceQuantity(resource: Resource) = storage.readQuantity(resource)
fun Game.readResourceStatus(resource: Resource) =
    ResourceStatus(this.readResourceQuantity(resource), this.readResourceMax(resource), resource)