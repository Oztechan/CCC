package mustafaozhan.github.com.mycurrencies.base.viewmodel

import mustafaozhan.github.com.mycurrencies.data.repository.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.MainData
import mustafaozhan.github.com.mycurrencies.util.enumValueOrNull
import org.joda.time.Duration
import org.joda.time.Instant

abstract class BaseDataViewModel(
    protected var preferencesRepository: PreferencesRepository
) : BaseViewModel() {

    companion object {
        const val NUMBER_OF_HOURS = 24
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
