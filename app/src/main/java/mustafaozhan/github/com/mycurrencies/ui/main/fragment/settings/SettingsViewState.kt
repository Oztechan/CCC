package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings

import mustafaozhan.github.com.mycurrencies.model.Currency

sealed class SettingsViewState

object NoResult : SettingsViewState()

object FewCurrency : SettingsViewState()

data class Success(val currencyList: MutableList<Currency>) : SettingsViewState()
