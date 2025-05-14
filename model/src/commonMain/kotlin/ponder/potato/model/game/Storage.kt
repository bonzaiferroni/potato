package ponder.potato.model.game

import kotlinx.serialization.Serializable

interface Storage {
    val limits: Map<Resource, Double>
    val resources: Map<Resource, Double>
    val aether: Double

    fun readQuantity(resource: Resource) = resources[resource] ?: 0.0
    fun addQuantity(resource: Resource, value: Double): Double
    fun removeQuantity(resource: Resource, value: Double): Boolean
    fun readLimit(resource: Resource) = limits[resource] ?: 0.0
}

@Serializable
data class GameStorage(
    override val resources: MutableMap<Resource, Double> = mutableMapOf()
): Storage {
    override val limits: MutableMap<Resource, Double> = mutableMapOf()

    override var aether get() = resources[Resource.Aether] ?: 0.0
        set(value) { resources[Resource.Aether] = value }

    override fun addQuantity(resource: Resource, value: Double): Double {
        val addedValue = minOf(readLimit(resource) - readQuantity(resource), value)
        resources[resource] = readQuantity(resource) + addedValue
        return addedValue
    }

    override fun removeQuantity(resource: Resource, value: Double): Boolean {
        val currentValue = readQuantity(resource)
        if (currentValue < value) return false
        resources[resource] = currentValue - value
        return true
    }
}

enum class Resource(val label: String) {
    Aether("The Aether"),
    Dirt("Dirt"),
    Gold("Gold"),
}