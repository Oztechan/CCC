package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view

import mustafaozhan.github.com.mycurrencies.model.Rates

sealed class ViewState

object LoadingState : ViewState()

object EmptyState : ViewState()

data class SuccessState(val rates: Rates) : ViewState()
