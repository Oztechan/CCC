package mustafaozhan.github.com.mycurrencies.ui.main

import mustafaozhan.github.com.basemob.viewmodel.BaseViewModel
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.MainData
import mustafaozhan.github.com.mycurrencies.tool.enumValueOrNull
import org.joda.time.Duration
import org.joda.time.Instant

abstract class MainDataViewModel(
    protected var preferencesRepository: PreferencesRepository
) : BaseViewModel() {

    companion object {
        const val NUMBER_OF_HOURS = 24
        const val MINIMUM_ACTIVE_CURRENCY = 2
    }

    internal val mainData: MainData
        get() = preferencesRepository.loadMainData()

    internal val isRewardExpired: Boolean
        get() = preferencesRepository.loadMainData().adFreeActivatedDate?.let {
            Duration(it, Instant.now()).standardHours > NUMBER_OF_HOURS
        } ?: true

    internal fun setCurrentBase(newBase: String?) = preferencesRepository
        .updateMainData(currentBase = enumValueOrNull<Currencies>(newBase ?: "NULL"))
}
