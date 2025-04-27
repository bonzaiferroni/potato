package ponder.potato.server

import io.ktor.server.application.*
import klutch.server.configureSecurity
import ponder.potato.server.plugins.configureApiRoutes
import ponder.potato.server.plugins.configureCors
import ponder.potato.server.plugins.configureDatabases
import ponder.potato.server.plugins.configureLogging
import ponder.potato.server.plugins.configureSerialization
import ponder.potato.server.plugins.configureWebSockets

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureCors()
    configureSerialization()
    configureDatabases()
    configureSecurity()
    configureApiRoutes()
    configureWebSockets()
    configureLogging()
}