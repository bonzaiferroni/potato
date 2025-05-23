package ponder.potato

import kotlinx.serialization.Serializable
import pondui.ui.nav.AppRoute
import pondui.ui.nav.matchIdRoute

@Serializable
object StartRoute : AppRoute("Start")

@Serializable
object HelloRoute : AppRoute("Hello")

@Serializable
object DreamRoute : AppRoute("Dream")

@Serializable
object EntityListRoute : AppRoute("Entities")

@Serializable
object CaveRoute : AppRoute("Cave")

@Serializable
object GameDataRoute : AppRoute("Game Data")

@Serializable
object ProgramListRoute : AppRoute("Programs")

@Serializable
data class ExampleProfileRoute(val exampleId: Long) : AppRoute(TITLE, exampleId) {
    companion object {
        const val TITLE = "Example"
        fun matchRoute(path: String) = matchIdRoute(path, TITLE) { ExampleProfileRoute(it) }
    }
}

@Serializable
data class ZoneRoute(val zoneId: Int) : AppRoute(TITLE, zoneId.toLong()) {
    companion object {
        const val TITLE = "Zone"
        fun matchRoute(path: String) = matchIdRoute(path, TITLE) { ZoneRoute(it.toInt()) }
    }
}

@Serializable
data class ProgramProfileRoute(val programId: Int) : AppRoute(TITLE, programId.toLong()) {
    companion object {
        const val TITLE = "Program"
        fun matchRoute(path: String) = matchIdRoute(path, TITLE) { ProgramProfileRoute(it.toInt()) }
    }
}