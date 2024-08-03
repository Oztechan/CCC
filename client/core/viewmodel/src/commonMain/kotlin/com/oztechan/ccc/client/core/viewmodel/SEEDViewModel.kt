/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.core.viewmodel

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class SEEDViewModel<
    State : BaseState,
    Effect : BaseEffect,
    Event : BaseEvent,
    Data : BaseData
    >(
    initialState: State,
    initialData: Data? = null
) : BaseViewModel() {
    // region SEED
    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    private val _effect: MutableSharedFlow<Effect> = MutableSharedFlow()
    val effect = _effect.asSharedFlow()

    @Suppress("UNCHECKED_CAST")
    val event: Event by lazy { this as Event }

    lateinit var data: Data
    // endregion

    init {
        if (initialData != null) {
            this.data = initialData
        }
    }

    protected fun setState(newState: State.() -> State) {
        _state.value = state.value.newState()
    }

    protected suspend fun setEffect(newEffect: () -> Effect) {
        _effect.emit(newEffect())
    }

    protected fun sendEffect(newEffect: () -> Effect) {
        viewModelScope.launch {
            _effect.emit(newEffect())
        }
    }
}
