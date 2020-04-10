package mustafaozhan.github.com.mycurrencies.ui.main.activity

import androidx.lifecycle.MutableLiveData
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.ui.main.MainDataViewModel
import mustafaozhan.github.com.mycurrencies.ui.main.activity.view.MainViewEffect
import mustafaozhan.github.com.mycurrencies.ui.main.activity.view.MainViewEvent
import mustafaozhan.github.com.mycurrencies.ui.main.activity.view.MainViewState
import org.joda.time.Instant

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:50 PM on Arch Linux wit Love <3.
 */
class MainViewModel(
    preferencesRepository: PreferencesRepository
) : MainDataViewModel<MainViewEffect, MainViewEvent, MainViewState>(
    preferencesRepository
), MainViewEvent {

    fun updateAdFreeActivation() {
        preferencesRepository.updateMainData(adFreeActivatedDate = Instant.now())
    }

    override fun getViewEvent() = this as MainViewEvent
    override val viewEffectLiveData: MutableLiveData<MainViewEffect> = MutableLiveData()
    override val viewStateLiveData: MutableLiveData<MainViewState> = MutableLiveData()
}
