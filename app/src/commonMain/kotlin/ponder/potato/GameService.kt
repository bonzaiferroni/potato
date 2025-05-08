package ponder.potato

import androidx.compose.ui.graphics.Color
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
import ponder.potato.model.game.zones.NamingWay
import pondui.utils.darken
import pondui.utils.lighten
import potato.app.generated.resources.Res

class GameService() {
    val game: Game get() = engine

    fun update(delta: Double) {
        engine.update(delta)
    }

    fun save() {
        appSettings["game_data"] = json.encodeToString(game.toGameData())
    }

    suspend fun init() {
        initEngine()
    }

    fun reset() {
        CoroutineScope(Dispatchers.Default).launch {
            setEngine(GameData())
        }
    }

    companion object {
        private var engine: GameEngine = GameEngine()

        private suspend fun initEngine() {
            val gameData: GameData = try {
                val saved = appSettings.get<String>("game_data")
                if (saved == null) GameData()
                else json.decodeFromString(saved)
            } catch (e: Exception) {
                GameData()
            }
            setEngine(gameData)
        }

        private suspend fun setEngine(gameData: GameData) {
            var bytes = Res.readBytes("files/sprite_names.txt")
            val spriteNames = bytes.decodeToString(0, 0 + bytes.size).split('\n')
            bytes = Res.readBytes("files/shroom_names.txt")
            val shroomNames = bytes.decodeToString(0, 0 + bytes.size).split('\n')
            val namingWay = NamingWay(spriteNames, shroomNames)
            engine = generateGame(gameData, namingWay)
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

object ResourceColor {
    val aether = Color(0xffb13c91)
    val aetherLight = aether.lighten(.5f)
    val aetherDark = aether.darken(.25f)
}