package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view

import com.github.mustafaozhan.basemob.view.BaseViewState
import mustafaozhan.github.com.mycurrencies.model.Currency

sealed class CalculatorViewState : BaseViewState()

object LoadingState : CalculatorViewState()

object EmptyState : CalculatorViewState()

data class SuccessState(val currencyList: MutableList<Currency>?) : CalculatorViewState()
