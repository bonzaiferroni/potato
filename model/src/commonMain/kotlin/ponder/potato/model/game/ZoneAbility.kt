package ponder.potato.model.game

enum class ZoneAbility(
    val label: String,
    val verb: String,
    val description: String,
    val resource: Resource?,
) {
    DreamSprite(
        label = "Sprite",
        verb = "Dream",
        description = "Sprites provide extra Aether at the end of each dream and watch over Potato.",
        resource = Resource.Aether
    ),
    DreamShroom(
        label = "Shroom",
        verb = "Dream",
        description = "Shrooms hold more of The Aether and give sprites a cozy place to rest.",
        resource = Resource.Aether
    ),
    ResolveDream(
        label = "Resolve Dream",
        verb = "Resolve",
        description = "Find an understanding of the dream, to open the way to the next level.",
        resource = Resource.Aether
    ),
    DreamBard(
        label = "Bard",
        verb = "Dream",
        description = "Dream of a bard.",
        resource = Resource.Aether
    ),
    BuySeed(
        label = "Buy Seed",
        verb = "Buy",
        description = "Buy a seed.",
        resource = Resource.Gold
    ),
    DreamBot(
        label = "Bot",
        verb = "Dream",
        description = "Dream of a bot",
        resource = Resource.Aether
    )
}