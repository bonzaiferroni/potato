package ponder.potato.model.game

import ponder.potato.model.game.entities.Entity

typealias EntityMap = Map<Long, Entity>

inline fun <reified T> EntityMap.read(id: Long) = this[id] as? T