/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.android.ui.currencies

import androidx.lifecycle.ViewModel
import com.github.mustafaozhan.ccc.client.ui.currencies.CurrenciesUseCase

class CurrenciesViewModel(
    val useCase: CurrenciesUseCase
) : ViewModel() {
    override fun onCleared() {
        useCase.onDestroy()
        super.onCleared()
    }
}
