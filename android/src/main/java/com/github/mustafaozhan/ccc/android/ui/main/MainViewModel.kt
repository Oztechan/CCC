/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.android.ui.main

import androidx.lifecycle.ViewModel
import com.github.mustafaozhan.ccc.client.ui.main.MainUseCase

class MainViewModel(
    val useCase: MainUseCase
) : ViewModel() {
    override fun onCleared() {
        useCase.onDestroy()
        super.onCleared()
    }
}
