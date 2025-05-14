package ponder.potato.model.game

//interface Portal: Position {
//    val zone: Zone
//    override val x: Float
//    override val y: Float
//    override val zoneId get() = zone.id
//}

class Portal(
    val destination: GameZone,
    val point: Point,
    override val zoneId: Int,
    val remotePoint: Point
): Position {
    override val x get() = point.x
    override val y get() = point.y

    fun transport(entity: StateEntity<*>) {
        entity.enter(destination)
        entity.state.position.x = remotePoint.x
        entity.state.position.y = remotePoint.y
    }
}