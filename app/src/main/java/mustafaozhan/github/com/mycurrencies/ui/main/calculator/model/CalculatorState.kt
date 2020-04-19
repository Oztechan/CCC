package mustafaozhan.github.com.mycurrencies.ui.main.calculator.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.basemob.model.BaseState
import mustafaozhan.github.com.mycurrencies.model.Currency

@Suppress("ConstructorParameterNaming")
data class CalculatorState(
    private val _state: _CalculatorState
) : BaseState() {
    val currencyList: LiveData<MutableList<Currency>> = _state._currencyList
    val input: LiveData<String> = _state._input
    val base: LiveData<String> = _state._base
    val output: LiveData<String> = _state._output
    val symbol: LiveData<String> = _state._symbol
    val loading: LiveData<Boolean> = _state._loading
}

@Suppress("ClassNaming", "ClassName", "ConstructorParameterNaming")
data class _CalculatorState(
    val _currencyList: MediatorLiveData<MutableList<Currency>> = MediatorLiveData<MutableList<Currency>>(),
    val _input: MediatorLiveData<String> = MediatorLiveData<String>(),
    val _base: MediatorLiveData<String> = MediatorLiveData<String>(),
    val _output: MutableLiveData<String> = MutableLiveData(""),
    val _symbol: MutableLiveData<String> = MutableLiveData(""),
    val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
)
