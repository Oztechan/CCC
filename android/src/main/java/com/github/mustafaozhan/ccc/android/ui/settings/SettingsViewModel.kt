/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.android.ui.settings

import androidx.lifecycle.ViewModel
import com.github.mustafaozhan.ccc.client.ui.settings.SettingsUseCase

class SettingsViewModel(
    val useCase: SettingsUseCase
) : ViewModel() {
    override fun onCleared() {
        useCase.onDestroy()
        super.onCleared()
    }
}
