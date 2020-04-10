package mustafaozhan.github.com.mycurrencies.ui.main.activity

import com.github.mustafaozhan.basemob.viewmodel.BaseViewModel
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import org.joda.time.Duration
import org.joda.time.Instant

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:50 PM on Arch Linux wit Love <3.
 */
class MainViewModel(
    val preferencesRepository: PreferencesRepository
) : BaseViewModel() {

    companion object {
        const val NUMBER_OF_HOURS = 24
    }

    fun updateAdFreeActivation() {
        preferencesRepository.updateMainData(adFreeActivatedDate = Instant.now())
    }

    val isRewardExpired: Boolean
        get() = preferencesRepository.loadMainData().adFreeActivatedDate?.let {
            Duration(it, Instant.now()).standardHours > NUMBER_OF_HOURS
        } ?: true
}
