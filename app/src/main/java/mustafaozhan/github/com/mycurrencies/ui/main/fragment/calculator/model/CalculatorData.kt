package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.model

import com.github.mustafaozhan.basemob.model.BaseData
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.model.Rates

data class CalculatorData(val preferencesRepository: PreferencesRepository) : BaseData() {
    var rates: Rates? = null
    val isRewardExpired
        get() = preferencesRepository.isRewardExpired
    val currentBase
        get() = preferencesRepository.currentBase
}
