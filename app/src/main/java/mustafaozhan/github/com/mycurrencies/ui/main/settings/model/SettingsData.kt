package mustafaozhan.github.com.mycurrencies.ui.main.settings.model

import com.github.mustafaozhan.basemob.model.BaseData
import mustafaozhan.github.com.mycurrencies.model.Currency

data class SettingsData(
    var unFilteredList: MutableList<Currency> = mutableListOf()
) : BaseData()
