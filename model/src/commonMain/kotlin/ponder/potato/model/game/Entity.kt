package ponder.potato.model.game

import kotlinx.coroutines.flow.MutableSharedFlow

interface Entity {
    val id: Long
    val zone: Zone
    val state: EntityState
    val components: List<Component>
    val name: String
    val isObserved: Boolean

    val game get() = zone.game
    val console get() = game.console
    val position: Position get() = state.position

    fun showEffect(effect: Effect)
}

abstract class StateEntity<out T: EntityState>: Entity {

    abstract override val state: T
    abstract override val components: List<StateComponent<*>>
    open val abilities: List<() -> EntityAction?> = emptyList()

    val effects = MutableSharedFlow<Effect>(extraBufferCapacity = 1)
    override val isObserved get() = effects.subscriptionCount.value > 0

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

    open fun start() = {
        for (component in components) {
            component.start()
        }
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

    override fun showEffect(effect: Effect) {
        effects.tryEmit(effect)
    }

    inline fun showEffect(effect: () -> Effect) {
        if (isObserved) showEffect(effect())
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