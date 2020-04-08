package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view

import mustafaozhan.github.com.mycurrencies.model.Rates

sealed class ViewState

object Loading : ViewState()

object Error : ViewState()

object Empty : ViewState()

object FewCurrency : ViewState()

data class Success(val rates: Rates) : ViewState()

data class OfflineSuccess(val rates: Rates) : ViewState()

data class MaximumInput(var input: String) : ViewState()
