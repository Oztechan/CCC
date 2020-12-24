/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.android.ui.calculator

import androidx.lifecycle.viewModelScope
import com.github.mustafaozhan.ccc.android.base.BaseViewModel
import com.github.mustafaozhan.ccc.client.ui.calculator.CalculatorUseCase

class CalculatorViewModel(
    val useCase: CalculatorUseCase
) : BaseViewModel() {
    override fun onStart() {
        useCase.scope = viewModelScope
    }

    override fun onDestroy() {
        useCase.onDestroy()
    }
}
