package ponder.potato.model.game

class NamingWay(
    private val sprites: List<String> = emptyList(),
    private val shrooms: List<String> = emptyList(),
) {
    fun getName(entity: StateEntity<NameState>) = when (entity) {
        is Sprite -> sprites.takeIf { it.isNotEmpty() }?.random()
        is Shroom -> shrooms.takeIf { it.isNotEmpty() }?.random()
        else -> null
    }
}