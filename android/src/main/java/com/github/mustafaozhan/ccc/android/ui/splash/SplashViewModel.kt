/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.android.ui.splash

import androidx.lifecycle.ViewModel
import com.github.mustafaozhan.ccc.client.ui.splash.SplashUseCase

class SplashViewModel(
    val useCase: SplashUseCase
) : ViewModel() {
    override fun onCleared() {
        useCase.onDestroy()
        super.onCleared()
    }
}
