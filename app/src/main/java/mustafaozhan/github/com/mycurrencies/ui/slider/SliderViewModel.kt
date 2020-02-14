package mustafaozhan.github.com.mycurrencies.ui.slider

import io.reactivex.Completable
import mustafaozhan.github.com.mycurrencies.base.viewmodel.BaseDataViewModel
import mustafaozhan.github.com.mycurrencies.data.repository.PreferencesRepository

class SliderViewModel(
    preferencesRepository: PreferencesRepository
) : BaseDataViewModel(preferencesRepository) {

    override fun onLoaded(): Completable = Completable.complete()

    fun setSliderShown() = preferencesRepository.updateMainData(sliderShown = true)
}
