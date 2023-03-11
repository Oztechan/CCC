/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.viewmodel.calculator.model

sealed class ConversionState {
    data class Online(val lastUpdate: String?) : ConversionState()
    data class Cached(val lastUpdate: String?) : ConversionState()
    data class Offline(val lastUpdate: String?) : ConversionState()
    object Error : ConversionState()
    object None : ConversionState()
}
