package ponder.potato.model.game

interface Component {
    val state: EntityState
    val entity: Entity
    val zone: Zone
    val game: Game
}

abstract class StateComponent<T: EntityState>: Component {
    abstract override val entity: StateEntity<T>
    override val state get() = entity.state
    override val zone get() = entity.zone
    override val game get() = entity.game

    open fun init() { }
    open fun start() { }
    open fun update(delta: Double) { }
    open fun enter(zone: GameZone) { }
    open fun recycle() { }
}