// Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
package mustafaozhan.github.com.mycurrencies.ui.main.calculator.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.basemob.model.BaseState
import mustafaozhan.github.com.mycurrencies.model.Currency

data class CalculatorState(
    private val backing: CalculatorStateBacking
) : BaseState() {
    val currencyList: LiveData<MutableList<Currency>> = backing._currencyList
    val input: LiveData<String> = backing._input
    val base: LiveData<String> = backing._base
    val output: LiveData<String> = backing._output
    val symbol: LiveData<String> = backing._symbol
    val loading: LiveData<Boolean> = backing._loading
}

@Suppress("ConstructorParameterNaming")
data class CalculatorStateBacking(
    val _currencyList: MediatorLiveData<MutableList<Currency>> = MediatorLiveData<MutableList<Currency>>(),
    val _input: MediatorLiveData<String> = MediatorLiveData<String>(),
    val _base: MediatorLiveData<String> = MediatorLiveData<String>(),
    val _output: MutableLiveData<String> = MutableLiveData(""),
    val _symbol: MutableLiveData<String> = MutableLiveData(""),
    val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
)
