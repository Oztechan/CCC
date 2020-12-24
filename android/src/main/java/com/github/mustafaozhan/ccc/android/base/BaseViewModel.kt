/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.android.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    override fun onCleared() {
        onDestroy()
        super.onCleared()
    }

    abstract fun onStart()
    abstract fun onDestroy()
}
