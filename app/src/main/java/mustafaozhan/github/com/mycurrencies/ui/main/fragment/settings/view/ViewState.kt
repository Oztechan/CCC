package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view

import mustafaozhan.github.com.mycurrencies.model.Currency

sealed class ViewState

object NoResult : ViewState()

data class NoFilter(val shouldCleanBase: Boolean) : ViewState()

data class Success(val currencyList: MutableList<Currency>) : ViewState()
