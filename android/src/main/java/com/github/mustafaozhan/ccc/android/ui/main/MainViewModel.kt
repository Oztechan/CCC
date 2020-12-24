/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.android.ui.main

import androidx.lifecycle.viewModelScope
import com.github.mustafaozhan.ccc.android.base.BaseViewModel
import com.github.mustafaozhan.ccc.client.ui.main.MainUseCase

class MainViewModel(
    val useCase: MainUseCase
) : BaseViewModel() {
    override fun onStart() {
        useCase.scope = viewModelScope
    }

    override fun onDestroy() {
        useCase.onDestroy()
    }
}
