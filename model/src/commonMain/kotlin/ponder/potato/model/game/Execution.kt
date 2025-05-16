package ponder.potato.model.game

enum class Execution {
    Complete,
    Moving,
    InProgress,
    TargetNotFound,
    TargetStorageEmpty,
    TargetStorageFull,
    BotStorageFull,
    BotStorageEmpty,
}