/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.android.ui.bar

import androidx.lifecycle.viewModelScope
import com.github.mustafaozhan.ccc.android.base.BaseViewModel
import com.github.mustafaozhan.ccc.client.ui.bar.BarUseCase

class BarViewModel(
    val useCase: BarUseCase
) : BaseViewModel() {
    override fun onStart() {
        useCase.scope = viewModelScope
    }

    override fun onDestroy() {
        useCase.onDestroy()
    }
}
