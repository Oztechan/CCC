package com.oztechan.ccc.client.viewmodel.widget

import com.oztechan.ccc.client.base.BaseViewModel
import com.oztechan.ccc.client.storage.calculator.CalculatorStorage

class WidgetViewModel(private val calculatorStorage: CalculatorStorage) : BaseViewModel() {
    var currentBase: String = ""

    fun refreshWidgetData() {
        currentBase = calculatorStorage.currentBase
    }
}
