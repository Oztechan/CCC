package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.model

import com.github.mustafaozhan.basemob.model.BaseData
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository

class SettingsData(val preferencesRepository: PreferencesRepository) : BaseData() {
    val isRewardExpired
        get() = preferencesRepository.isRewardExpired
}
