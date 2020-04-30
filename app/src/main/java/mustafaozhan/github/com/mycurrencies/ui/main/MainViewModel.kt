/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.ui.main

import com.github.mustafaozhan.basemob.viewmodel.BaseViewModel
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import org.joda.time.Instant

class MainViewModel(
    private val preferencesRepository: PreferencesRepository
) : BaseViewModel() {

    fun updateAdFreeActivation() {
        preferencesRepository.updateMainData(adFreeActivatedDate = Instant.now())
    }

    fun isRewardExpired() = preferencesRepository.isRewardExpired

    fun isFirstRun() = preferencesRepository.firstRun
}
