package mustafaozhan.github.com.mycurrencies.ui.slider

import io.reactivex.Completable
import mustafaozhan.github.com.mycurrencies.base.BaseViewModel
import mustafaozhan.github.com.mycurrencies.data.repository.PreferencesRepository

class SliderViewModel(
    override val preferencesRepository: PreferencesRepository
) : BaseViewModel() {
    override fun onLoaded(): Completable {
        return Completable.complete()
    }
}
