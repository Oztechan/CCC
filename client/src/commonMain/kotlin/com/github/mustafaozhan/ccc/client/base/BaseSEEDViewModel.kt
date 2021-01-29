/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.base

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseSEEDViewModel : BaseViewModel() {
    // region SEED
    abstract val state: StateFlow<BaseState>?
    abstract val effect: Flow<BaseEffect>?
    abstract val event: BaseEvent?
    abstract val data: BaseData?
    // endregion
}
