package mustafaozhan.github.com.mycurrencies.ui.main.settings.model

import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.ui.main.MainActivityData

data class SettingsData(
    var unFilteredList: MutableList<Currency> = mutableListOf()
) : MainActivityData()
