package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.basemob.view.BaseViewState
import mustafaozhan.github.com.mycurrencies.model.Currency

data class CalculatorViewState(val observer: CalculatorViewStateObserver) : BaseViewState() {
    val input: MutableLiveData<String> = observer.input
    val currencyList: MutableLiveData<MutableList<Currency>> = MutableLiveData(mutableListOf())
    val output: MutableLiveData<String> = MutableLiveData("")
    val symbol: MutableLiveData<String> = MutableLiveData("")
    val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val empty: MutableLiveData<Boolean> = MutableLiveData(true)
}

data class CalculatorViewStateObserver(
    val input: MediatorLiveData<String> = MediatorLiveData<String>()
)
