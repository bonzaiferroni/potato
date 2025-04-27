package ponder.potato.model.game

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class StateEngine<State>(
    initialState: State,
) {
    protected val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()
    val stateNow get() = state.value

    protected fun setState(block: (State) -> State) {
        _state.value = block(state.value)
    }

    private val engines = mutableListOf<StateEngine<*>>()

    infix fun <S, E : StateEngine<S>> add(engine: E) = engine.also { engines.add(engine) }

    open fun update(delta: Double) {
        for (engine in engines) {
            engine.update(delta)
        }
    }
}