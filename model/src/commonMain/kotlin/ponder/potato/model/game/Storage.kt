package ponder.potato.model.game

import kotlinx.serialization.Serializable

interface Storage {
    val limits: Map<Resource, Double>
    val resources: Map<Resource, Double>
    val items: Map<Item, Int>
    val aether: Double

    fun readQuantity(resource: Resource) = resources[resource] ?: 0.0
    fun addQuantity(resource: Resource, value: Double): Double
    fun removeQuantity(resource: Resource, value: Double): Boolean
    fun readLimit(resource: Resource) = limits[resource] ?: 0.0
    fun readQuantity(item: Item) = items[item] ?: 0
    fun addQuantity(item: Item, value: Int)
    fun removeQuantity(item: Item, value: Int): Boolean
}

@Serializable
data class GameStorage(
    override val resources: MutableMap<Resource, Double> = mutableMapOf(),
    override val items: MutableMap<Item, Int> = mutableMapOf()
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

    override fun addQuantity(item: Item, value: Int) {
        items[item] = readQuantity(item) + value
    }

    override fun removeQuantity(item: Item, value: Int): Boolean {
        val currentValue = readQuantity(item)
        if (currentValue < value) return false
        items[item] = currentValue - value
        return true
    }
}

enum class Resource(val label: String) {
    Aether("The Aether"),
    Dirt("Dirt"),
    Gold("Gold"),
}

enum class Item(val label: String) {
    Seed("Seed")
}