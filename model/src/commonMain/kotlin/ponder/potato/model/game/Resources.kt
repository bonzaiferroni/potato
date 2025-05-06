package ponder.potato.model.game

import kotlinx.serialization.Serializable

interface Resources {
    val map: Map<Resource, Double>
    val aether: Double
}

@Serializable
data class GameResources(
    override val map: MutableMap<Resource, Double> = mutableMapOf(
        Resource.Aether to 0.0
    )
): Resources {
    override var aether get() = map[Resource.Aether] ?: 0.0
        set(value) { map[Resource.Aether] = value }
}

enum class Resource {
    Aether
}