/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.ui.main.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.basemob.model.BaseEffect
import com.github.mustafaozhan.basemob.model.BaseEvent
import com.github.mustafaozhan.basemob.model.BaseState
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.model.Rates
import mustafaozhan.github.com.mycurrencies.ui.main.MainData

data class CalculatorState(
    private val backing: CalculatorStateBacking
) : BaseState() {
    val input: LiveData<String> = backing._input
    val base: LiveData<String> = backing._base
    val currencyList: LiveData<MutableList<Currency>> = backing._currencyList
    val output: LiveData<String> = backing._output
    val symbol: LiveData<String> = backing._symbol
    val loading: LiveData<Boolean> = backing._loading
}

@Suppress("ConstructorParameterNaming")
data class CalculatorStateBacking(
    val _input: MediatorLiveData<String> = MediatorLiveData<String>(),
    val _base: MediatorLiveData<String> = MediatorLiveData<String>(),
    val _currencyList: MutableLiveData<MutableList<Currency>> = MutableLiveData<MutableList<Currency>>(),
    val _output: MutableLiveData<String> = MutableLiveData(""),
    val _symbol: MutableLiveData<String> = MutableLiveData(""),
    val _loading: MutableLiveData<Boolean> = MutableLiveData(true)
)

interface CalculatorEvent : BaseEvent {
    fun onKeyPress(key: String)
    fun onItemClick(currency: Currency, conversion: String)
    fun onItemLongClick(currency: Currency): Boolean
    fun onBarClick()
    fun onSpinnerItemSelected(base: String)
}

sealed class CalculatorEffect : BaseEffect()
object ErrorEffect : CalculatorEffect()
object FewCurrencyEffect : CalculatorEffect()
object ReverseSpinner : CalculatorEffect()
object MaximumInputEffect : CalculatorEffect()
data class OfflineSuccessEffect(val date: String?) : CalculatorEffect()
data class LongClickEffect(val text: String, val name: String) : CalculatorEffect()

data class CalculatorData(
    var rates: Rates? = null
) : MainData() {
    companion object {
        internal const val MAXIMUM_INPUT = 15
        internal const val KEY_DEL = "DEL"
        internal const val KEY_AC = "AC"
        internal const val CHAR_DOT = '.'
        internal const val KEY_BASE_CURRENCY = "base_currency"
    }
}
