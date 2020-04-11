package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view

import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.basemob.view.BaseViewState
import mustafaozhan.github.com.mycurrencies.model.Currency

data class CalculatorViewState(
    val currencyList: MutableLiveData<MutableList<Currency>> = MutableLiveData(mutableListOf()),
    val input: MutableLiveData<String> = MutableLiveData(""),
    val output: MutableLiveData<String> = MutableLiveData(""),
    val symbol: MutableLiveData<String> = MutableLiveData(""),
    val loading: MutableLiveData<Boolean> = MutableLiveData(false),
    val empty: MutableLiveData<Boolean> = MutableLiveData(true)
) : BaseViewState()
