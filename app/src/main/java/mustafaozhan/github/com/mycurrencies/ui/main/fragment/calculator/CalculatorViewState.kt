package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator

import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.model.Rates

sealed class CalculatorViewState {

    object Loading : CalculatorViewState()

    object Empty : CalculatorViewState()

    object FewCurrency : CalculatorViewState()

    object MaximumInput : CalculatorViewState()

    data class Error(val isFewCurrency: Boolean) : CalculatorViewState()

    data class Success(
        val currencyList: MutableList<Currency>?,
        val baseCurrency: Currencies
    ) : CalculatorViewState()

    data class OfflineSuccess(
        val currencyList: MutableList<Currency>?,
        val baseCurrency: Currencies,
        val rates: Rates
    ) : CalculatorViewState()
}
