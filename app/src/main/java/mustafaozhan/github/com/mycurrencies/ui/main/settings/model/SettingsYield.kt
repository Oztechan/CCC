package mustafaozhan.github.com.mycurrencies.ui.main.settings.model

import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.ui.main.MainYield

data class SettingsYield(
    var unFilteredList: MutableList<Currency> = mutableListOf()
) : MainYield()
