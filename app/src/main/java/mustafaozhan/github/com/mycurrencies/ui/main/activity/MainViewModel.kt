package mustafaozhan.github.com.mycurrencies.ui.main.activity

import mustafaozhan.github.com.mycurrencies.base.viewmodel.BaseDataViewModel
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import org.joda.time.Instant

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:50 PM on Arch Linux wit Love <3.
 */
class MainViewModel(
    preferencesRepository: PreferencesRepository
) : BaseDataViewModel(preferencesRepository) {

    fun updateAdFreeActivation() {
        preferencesRepository.updateMainData(adFreeActivatedDate = Instant.now())
    }
}
