/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.android.model

sealed class DataState {
    data class Online(val lastUpdate: String?) : DataState()
    data class Cached(val lastUpdate: String?) : DataState()
    data class Offline(val lastUpdate: String?) : DataState()
    object Error : DataState()
}
