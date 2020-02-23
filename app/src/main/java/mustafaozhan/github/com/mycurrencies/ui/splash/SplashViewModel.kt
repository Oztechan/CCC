package mustafaozhan.github.com.mycurrencies.ui.splash

import io.reactivex.Completable
import mustafaozhan.github.com.mycurrencies.base.viewmodel.BaseDataViewModel
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository

class SplashViewModel(
    preferencesRepository: PreferencesRepository
) : BaseDataViewModel(preferencesRepository) {

    override fun onLoaded(): Completable = Completable.complete()

    fun isSliderShown() = preferencesRepository.loadMainData().sliderShown == true
}
