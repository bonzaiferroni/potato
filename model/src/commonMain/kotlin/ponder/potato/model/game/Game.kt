package ponder.potato.model.game

interface Game {
    val state: GameState
    val zones: List<GameZone>
    val entities: Map<Long, Entity>
    val storage: Storage
    val namingWay: NamingWay
    val console: GameConsole
    val programs: Map<Int, Program>

    val potato get() = entities.readEntity<Potato>()

    fun getZone(zoneId: Int) = zones.first { it.id == zoneId }

    val dreamLevel get() = potato?.state?.level ?: 1
}

fun Game.readShroomStorage() =
    entities.sumOf<EntityStorageState>({ it is Shroom }) { it.capacity }
fun Game.readSpriteAether() =
    entities.sumOf<DreamerState>({ it is Sprite }) { it.getReward(dreamLevel) }
fun Game.readResourceMax(resource: Resource) =
    entities.sumOf<EntityStorageState> { if (it.isStorageType(resource)) it.capacity else 0.0 }
fun Game.readResourceQuantity(resource: Resource) = storage.readQuantity(resource)
fun Game.readResourceStatus(resource: Resource) =
    ResourceStatus(this.readResourceQuantity(resource), this.readResourceMax(resource), resource)

fun Game.readAllInstructions(): List<Instruction> {
    val instructions = mutableListOf<Instruction>()
    for ((id, entity) in entities) {
        val source = entity as? InstructionSource ?: continue
        source.addInstructions(instructions)
    }
    for (zone in zones) {
        val source = zone as? InstructionSource ?: continue
        source.addInstructions(instructions)
    }
    return instructions
}