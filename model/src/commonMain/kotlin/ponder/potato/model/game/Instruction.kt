package ponder.potato.model.game

import kotlinx.serialization.Serializable

@Serializable
sealed class Instruction {
    abstract fun execute(game: Game, bot: Bot, delta: Double): Execution
}

@Serializable
class TakeResource(
    val zoneId: Int,
    val resource: Resource,
): Instruction() {

    override fun execute(game: Game, bot: Bot, delta: Double): Execution {
        val entity = game.entities.read<StateEntity<EntityStorageState>> {
            it.zone.id == zoneId && it.state.resource == resource
        }
        if (entity == null) return Execution.TargetNotFound
        val heldResource = bot.state.resource
        if (heldResource != null && heldResource != entity.state.resource || bot.state.capacityAvailable == 0.0)
            return Execution.BotStorageFull

        val isArrived = bot.moveTo(entity.position, delta)
        if (!isArrived) return Execution.Moving

        val removed = game.storage.removeQuantity(resource, bot.state.capacityAvailable)
        if (!removed) return Execution.TargetStorageEmpty
        bot.state.stored += bot.state.capacityAvailable
        bot.state.resource = entity.state.resource
        return Execution.Complete
    }
}

@Serializable
class FillResource(
    val zoneId: Int,
    val resource: Resource,
): Instruction() {
    override fun execute(game: Game, bot: Bot, delta: Double): Execution {
        val target = game.entities.read<StateEntity<ResourceConsumerState>> {
            it.zone.id == zoneId && it.state.resource == resource
        }
        if (target == null) return Execution.TargetNotFound
        val heldResource = bot.state.resource
        if (heldResource != null && heldResource != target.state.resource || bot.state.stored == 0.0)
            return Execution.BotStorageEmpty

        val isArrived = bot.moveTo(target.position, delta)
        if (!isArrived) return Execution.Moving

        if (target.state.isFull) return Execution.TargetStorageFull
        val transferredQuantity = minOf(target.state.capacityAvailable, bot.state.stored)
        bot.state.stored -= transferredQuantity
        target.state.stored += transferredQuantity
        if (bot.state.isEmpty) bot.state.resource = null
        return Execution.Complete
    }
}

@Serializable
class MineTarget(
    val zoneId: Int,
): Instruction() {
    override fun execute(game: Game, bot: Bot, delta: Double): Execution {
        val target = game.entities.readWithZoneId<StateEntity<MinerTargetState>>(zoneId)
        if (target == null) return Execution.TargetNotFound

        val isArrived = bot.moveTo(target.position, delta)
        if (!isArrived) return Execution.Moving

        bot.state.targetId = target.id
        bot.state.intent = Intent.Mine

        if ((bot.state.progress ?: 0f) < 1f) return Execution.InProgress

        return Execution.Complete
    }
}