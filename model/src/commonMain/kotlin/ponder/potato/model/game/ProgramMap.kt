package ponder.potato.model.game

typealias ProgramMap = Map<Int, Program>

fun ProgramMap.getNextId() = this.keys.max() + 1
fun ProgramMap.add(program: Program) {
    (this as MutableMap<Int, Program>)[getNextId()] = program
}