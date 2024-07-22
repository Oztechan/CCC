/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.core.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow

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

    abstract val effect: SharedFlow<Effect>?
    abstract val event: Event?
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
}
