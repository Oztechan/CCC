/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.ui.main.settings.model

import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.ui.main.MainActivityData

data class SettingsData(
    var unFilteredList: MutableList<Currency> = mutableListOf()
) : MainActivityData() {
    companion object {
        internal const val ACTIVE = 1
        internal const val DE_ACTIVE = 0
    }
}
