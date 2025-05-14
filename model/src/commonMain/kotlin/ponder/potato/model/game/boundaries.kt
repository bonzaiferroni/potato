package ponder.potato.model.game

import kabinet.utils.random

const val BOUNDARY = 5f
const val INSIDE_BOUNDARY = BOUNDARY - 1

fun Float.Companion.randomInsideBoundary() = Float.random(-INSIDE_BOUNDARY, INSIDE_BOUNDARY)
