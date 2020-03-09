package mustafaozhan.github.com.mycurrencies.ui.main.activity

import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.ui.main.MainDataViewModel
import org.joda.time.Instant

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:50 PM on Arch Linux wit Love <3.
 */
class MainViewModel(
    preferencesRepository: PreferencesRepository
) : MainDataViewModel(preferencesRepository) {

    fun updateAdFreeActivation() {
        preferencesRepository.updateMainData(adFreeActivatedDate = Instant.now())
    }
}
