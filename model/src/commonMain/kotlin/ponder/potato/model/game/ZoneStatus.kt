package ponder.potato.model.game

sealed class ZoneStatus {
}

data class ResourceStatus(
    val current: Double,
    val max: Double,
    val resource: Resource
): ZoneStatus()

data class IntValue(
    val label: String,
    val value: Int,
): ZoneStatus()

data class ProgressValue(
    val label: String,
    val progress: Float,
): ZoneStatus()