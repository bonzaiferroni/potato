package ponder.potato

import com.russhwolf.settings.get
import com.russhwolf.settings.set
import io.ktor.http.ContentDisposition.Companion.File
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import ponder.potato.model.game.GameData
import ponder.potato.model.game.entities.BardState
import ponder.potato.model.game.entities.EntityState
import ponder.potato.model.game.entities.ImpState
import ponder.potato.model.game.entities.PotatoState
import ponder.potato.model.game.entities.ShroomState
import ponder.potato.model.game.entities.SpriteState
import ponder.potato.model.game.generateGame
import ponder.potato.model.game.zones.Game
import ponder.potato.model.game.zones.GameEngine

class GameService() {
    val game: Game get() = engine

    fun update(delta: Double) {
        engine.update(delta)
    }

    fun save() {
        appSettings["game_data"] = json.encodeToString(game.toGameData())
    }

    companion object {
        private val engine: GameEngine
        init {
            val gameData: GameData = try {
                val saved = appSettings.get<String>("game_data")
                if (saved == null) GameData()
                else json.decodeFromString(saved)
            } catch (e: Exception) {
                GameData()
            }
            engine = generateGame(gameData)
        }
    }
}

val gameSerialModule = SerializersModule {
    polymorphic(EntityState::class) {
        subclass(PotatoState::class, PotatoState.serializer())
        subclass(SpriteState::class, SpriteState.serializer())
        subclass(ShroomState::class, ShroomState.serializer())
        subclass(ImpState::class, ImpState.serializer())
        subclass(BardState::class, BardState.serializer())
        // Add all yer other entity state types here
    }
}

val json = Json {
    serializersModule = gameSerialModule
    classDiscriminator = "type" // or "kind", or whatever suits yer flag
    ignoreUnknownKeys = true
}
