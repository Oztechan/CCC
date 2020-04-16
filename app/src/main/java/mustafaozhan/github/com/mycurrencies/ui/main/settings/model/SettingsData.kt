package mustafaozhan.github.com.mycurrencies.ui.main.settings.model

import com.github.mustafaozhan.basemob.model.BaseData
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.model.Currency

class SettingsData(val preferencesRepository: PreferencesRepository) : BaseData() {
    val isRewardExpired
        get() = preferencesRepository.isRewardExpired
    var unFilteredList: MutableList<Currency> = mutableListOf()
}
