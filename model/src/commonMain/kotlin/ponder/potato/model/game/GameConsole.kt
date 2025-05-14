package ponder.potato.model.game

import kotlinx.coroutines.flow.MutableSharedFlow

class GameConsole {

    val messages = MutableSharedFlow<String>(extraBufferCapacity = 1)

    fun isObserved(entity: Entity) = true
    fun log(message: String) {
        messages.tryEmit(message)
    }
    inline fun log(entity: Entity, message: () -> String) {
        if (isObserved(entity)) log(message())
    }
}