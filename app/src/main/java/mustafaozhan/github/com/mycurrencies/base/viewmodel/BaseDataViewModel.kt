package mustafaozhan.github.com.mycurrencies.base.viewmodel

import mustafaozhan.github.com.mycurrencies.data.repository.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.model.Currencies
import org.joda.time.Duration
import org.joda.time.Instant

abstract class BaseDataViewModel(
    protected var preferencesRepository: PreferencesRepository
) : BaseViewModel() {

    companion object {
        const val NUMBER_OF_HOURS = 24
    }

    open fun setCurrentBase(newBase: String?) {
        preferencesRepository.updateMainData(currentBase = Currencies.valueOf(newBase ?: "NULL"))
    }

    open fun isSliderShown() = preferencesRepository.loadMainData().sliderShown

    open fun setSliderShown() {
        preferencesRepository.updateMainData(sliderShown = true)
    }

    open fun getMainData() = preferencesRepository.loadMainData()

    open fun isRewardExpired() = preferencesRepository.loadMainData().adFreeActivatedDate?.let {
        Duration(it, Instant.now()).standardHours > NUMBER_OF_HOURS
    } ?: true
}
