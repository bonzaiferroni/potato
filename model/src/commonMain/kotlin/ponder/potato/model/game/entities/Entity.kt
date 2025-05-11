package ponder.potato.model.game.entities

import kotlinx.coroutines.flow.MutableSharedFlow
import ponder.potato.model.game.Effect
import ponder.potato.model.game.EntityMap
import ponder.potato.model.game.MutablePosition
import ponder.potato.model.game.Position
import ponder.potato.model.game.components.Component
import ponder.potato.model.game.components.SpiritState
import ponder.potato.model.game.components.StateComponent
import ponder.potato.model.game.components.TargetState
import ponder.potato.model.game.findNearest
import ponder.potato.model.game.read
import ponder.potato.model.game.zones.EntityAction
import ponder.potato.model.game.zones.GameZone
import ponder.potato.model.game.zones.Zone

interface Entity {
    val id: Long
    val zone: Zone
    val state: EntityState
    val components: List<Component>

    val game get() = zone.game
    val position: Position get() = state.position

    fun showEffect(createEffect: () -> Effect)
}

abstract class StateEntity<out T: EntityState>: Entity {

    abstract override val state: T
    abstract override val components: List<StateComponent<*>>
    open val abilities: List<() -> EntityAction?> = emptyList()

    val effects = MutableSharedFlow<Effect>(extraBufferCapacity = 1)

    override var id = 0L

    private var _zone: GameZone? = null
    override val zone get() = _zone ?: error("zone not initialized")
    val isIdle get() = state.intent == null

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
        (state as? TargetState)?.let { it.targetId = null }
    }

    open fun update(delta: Double) {
        for (component in components) {
            component.update(delta)
        }
    }

    open fun recycle() {
        for (component in components) {
            component.recycle()
        }
    }

    override fun showEffect(createEffect: () -> Effect) {
        if (effects.subscriptionCount.value == 0) return
        effects.tryEmit(createEffect())
    }

    fun hasIntent(intent: Intent) = state.intent == intent
    fun hasOtherIntent(intent: Intent) = state.intent != null && state.intent != intent
}

interface EntityState {
    val isAlive: Boolean
    val position: MutablePosition
    var log: String?
    var intent: Intent?
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