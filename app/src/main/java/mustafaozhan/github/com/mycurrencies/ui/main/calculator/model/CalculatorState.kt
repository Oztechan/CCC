package mustafaozhan.github.com.mycurrencies.ui.main.calculator.model

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.basemob.model.BaseState
import mustafaozhan.github.com.mycurrencies.model.Currency

data class CalculatorState(val mediator: CalculatorStateMediator) : BaseState() {
    val input: MutableLiveData<String> = mediator.input
    val base: MutableLiveData<String> = mediator.base
    val currencyList: MutableLiveData<MutableList<Currency>> = MutableLiveData(mutableListOf())
    val output: MutableLiveData<String> = MutableLiveData("")
    val symbol: MutableLiveData<String> = MutableLiveData("")
    val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val empty: MutableLiveData<Boolean> = MutableLiveData(true)
}

data class CalculatorStateMediator(
    val input: MediatorLiveData<String> = MediatorLiveData<String>(),
    val base: MediatorLiveData<String> = MediatorLiveData<String>()
)
