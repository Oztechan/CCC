/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.base

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseSEEDViewModel : BaseViewModel() {
    // region SEED
    abstract val state: StateFlow<BaseState>?
    abstract val effect: SharedFlow<BaseEffect>?
    abstract val event: BaseEvent?
    abstract val data: BaseData?
    // endregion
}
