package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view

import com.github.mustafaozhan.basemob.view.BaseViewState
import mustafaozhan.github.com.mycurrencies.model.Currency

sealed class SettingsViewState : BaseViewState()

object NoResult : SettingsViewState()

data class Success(val currencyList: MutableList<Currency>) : SettingsViewState()
