package ponder.potato.model.game

interface Resources {
    val map: Map<Resource, Double>
    val aether: Double
}

data class GameResources(
    override val map: MutableMap<Resource, Double> = mutableMapOf(
        Resource.Aether to 1000.0
    )
): Resources {
    override var aether get() = map.getValue(Resource.Aether)
        set(value) { map[Resource.Aether] = value }
}

enum class Resource {
    Aether
}