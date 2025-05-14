package ponder.potato.model.game

import kotlinx.coroutines.flow.MutableSharedFlow

interface Entity {
    val id: Long
    val zone: Zone
    val state: EntityState
    val components: List<Component>
    val name: String

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
    override val name get() = this::class.simpleName ?: error("must used named entity class")

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
    inline fun <reified T: EntityState> readTarget() = target?.castIfState<T>()
}

interface EntityState {
    val isAlive: Boolean
    val position: MutablePosition
    var intent: Intent?
    val speed get() = 1f
}