/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.viewmodel

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseSEEDViewModel<
    State : BaseState,
    Effect : BaseEffect,
    Event : BaseEvent,
    Data : BaseData
    > : BaseViewModel() {
    // region SEED
    abstract val state: StateFlow<State>?
    abstract val effect: SharedFlow<Effect>?
    abstract val event: Event?
    abstract val data: Data?
    // endregion
}
