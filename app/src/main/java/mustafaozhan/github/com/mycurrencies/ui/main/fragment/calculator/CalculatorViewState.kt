package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator

import mustafaozhan.github.com.mycurrencies.model.Rates

sealed class CalculatorViewState {

    object Loading : CalculatorViewState()

    data class BackEndSuccess(val rates: Rates) : CalculatorViewState()

    data class DataBaseSuccess(val rates: Rates) : CalculatorViewState()

    object Error : CalculatorViewState()
}
