package ponder.potato.model.game

import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

fun EntityState.toEntity() = when(this) {
    is PotatoState -> Potato(this)
    is SpriteState -> Sprite(this)
    is ShroomState -> Shroom(this)
    is BardState -> Bard(this)
    is ImpState -> Imp(this)
    is OutcropState -> Outcrop(this)
    is VaultState -> Vault(this)
    is DirtPileState -> DirtPile(this)
    is GardenBedState -> GardenBed(this)
    is EngineerState -> Engineer(this)
    is BotState -> Bot(this)
    else -> error("unknown state: ${this::class.simpleName}")
}

val gameSerialModule = SerializersModule {
    polymorphic(EntityState::class) {
        subclass(PotatoState::class, PotatoState.serializer())
        subclass(SpriteState::class, SpriteState.serializer())
        subclass(ShroomState::class, ShroomState.serializer())
        subclass(ImpState::class, ImpState.serializer())
        subclass(BardState::class, BardState.serializer())
        subclass(OutcropState::class, OutcropState.serializer())
        subclass(VaultState::class, VaultState.serializer())
        subclass(DirtPileState::class, DirtPileState.serializer())
        subclass(GardenBedState::class, GardenBedState.serializer())
        subclass(EngineerState::class, EngineerState.serializer())
        subclass(BotState::class, BotState.serializer())
        // Add all yer other entity state types here
    }
}