package ponder.potato

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform