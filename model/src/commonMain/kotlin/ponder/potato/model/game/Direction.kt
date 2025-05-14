package ponder.potato.model.game

enum class Direction(val x: Int, val y: Int) {
    North(0, 1),
    NorthEast(1, 1),
    East(1, 0),
    SouthEast(1, -1),
    South(0, -1),
    SouthWest(-1, -1),
    West(-1, 0),
    NorthWest(-1, 1);

    val boundaryPoint = Vector2(x * BOUNDARY, y * BOUNDARY)
    val midPoint = Vector2(x * 2f, y * 2f)

    val opposite get() = when (this) {
        North -> South
        NorthEast -> SouthWest
        East -> West
        SouthEast -> NorthWest
        South -> North
        SouthWest -> NorthEast
        West -> East
        NorthWest -> SouthEast
    }
}