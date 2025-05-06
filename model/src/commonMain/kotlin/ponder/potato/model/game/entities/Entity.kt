package ponder.potato.model.game.entities

import kotlinx.coroutines.flow.MutableSharedFlow
import ponder.potato.model.game.Effect
import ponder.potato.model.game.MutablePosition
import ponder.potato.model.game.Position
import ponder.potato.model.game.components.Component
import ponder.potato.model.game.components.StateComponent
import ponder.potato.model.game.zones.GameZone
import ponder.potato.model.game.zones.Zone

interface Entity {
    val id: Long
    val zone: Zone
    val state: EntityState
    val components: List<Component>

    val game get() = zone.game
    val position: Position get() = state.position
}

abstract class StateEntity<out T: EntityState>: Entity {

    abstract override val state: T
    abstract override val components: List<StateComponent<*>>

    val effects = MutableSharedFlow<Effect>(extraBufferCapacity = 1)

    override var id = 0L

    private var _zone: GameZone? = null
    override val zone get() = _zone ?: error("zone not initialized")

    open fun enter(zone: GameZone) {
        state.position.zoneId = zone.id
        _zone = zone
        for (component in components) {
            component.enter(zone)
        }
    }

    open fun init(id: Long) {
        this.id = id
        for (component in components) {
            component.init()
        }
    }

    open fun update(delta: Double) {
        for (component in components) {
            component.update(delta)
        }
    }

    fun showEffect(createEffect: () -> Effect) {
        if (effects.subscriptionCount.value == 0) return
        effects.tryEmit(createEffect())
    }
}

interface EntityState {
    val isAlive: Boolean
    val position: MutablePosition
    val speed get() = 1f

    fun toEntity() = when(this) {
        is PotatoState -> Potato(this)
        is SpriteState -> Sprite(this)
        is ShroomState -> Shroom(this)
        is BardState -> Bard(this)
        is ImpState -> Imp(this)
        else -> error("unknown state: ${this::class.simpleName}")
    }
}