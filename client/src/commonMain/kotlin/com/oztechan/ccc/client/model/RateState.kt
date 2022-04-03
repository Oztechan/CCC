/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.model

sealed class RateState {
    data class Online(val lastUpdate: String?) : RateState()
    data class Cached(val lastUpdate: String?) : RateState()
    data class Offline(val lastUpdate: String?) : RateState()
    object Error : RateState()
    object None : RateState()
}
