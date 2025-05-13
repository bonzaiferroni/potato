package ponder.potato.model.game

interface VisitorState: EntityState {
    var visitorId: Long?
}