/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.android.ui.calculator

import androidx.lifecycle.ViewModel
import com.github.mustafaozhan.ccc.client.ui.calculator.CalculatorUseCase

class CalculatorViewModel(
    val useCase: CalculatorUseCase
) : ViewModel() {
    override fun onCleared() {
        useCase.onDestroy()
        super.onCleared()
    }
}
