package mustafaozhan.github.com.mycurrencies.ui.main

import com.github.mustafaozhan.basemob.viewmodel.BaseViewModel
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import org.joda.time.Instant

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:50 PM on Arch Linux wit Love <3.
 */
class MainViewModel(
    private val preferencesRepository: PreferencesRepository
) : BaseViewModel() {

    fun updateAdFreeActivation() {
        preferencesRepository.updateMainData(adFreeActivatedDate = Instant.now())
    }

    fun isRewardExpired() = preferencesRepository.isRewardExpired
}
