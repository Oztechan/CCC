/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.android.ui.bar

import androidx.lifecycle.ViewModel
import com.github.mustafaozhan.ccc.client.ui.bar.BarUseCase

class BarViewModel(
    val useCase: BarUseCase
) : ViewModel() {
    override fun onCleared() {
        useCase.onDestroy()
        super.onCleared()
    }
}
