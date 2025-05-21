package ponder.potato

import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ponder.potato.model.game.GameData
import ponder.potato.model.game.generateGame
import ponder.potato.model.game.Game
import ponder.potato.model.game.GameEngine
import ponder.potato.model.game.NamingWay
import ponder.potato.model.game.gameSerialModule
import ponder.potato.model.game.toGameData
import potato.app.generated.resources.Res

class GameService() {
    val game: Game get() = engine
    val messages: List<String> = _messages

    fun update(delta: Double) {
        engine.update(delta)
    }

    fun save() {
        appSettings["game_data"] = json.encodeToString(engine.toGameData())
    }

    suspend fun init() {
        messageCollection?.cancel()

        initEngine()
        engine.start()
        messageCollection = CoroutineScope(Dispatchers.Default).launch {
            engine.console.messages.collect {
                _messages.add(it)
                if (_messages.size > 100) _messages.removeFirst()
            }
        }
    }

    fun reset() {
        CoroutineScope(Dispatchers.Default).launch {
            setEngine(GameData())
        }
    }

    companion object {
        private var engine: GameEngine = GameEngine()
        private val _messages = ArrayDeque<String>()
        var messageCollection: Job? = null

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

val json = Json {
    serializersModule = gameSerialModule
    classDiscriminator = "type" // or "kind", or whatever suits yer flag
    ignoreUnknownKeys = true
}