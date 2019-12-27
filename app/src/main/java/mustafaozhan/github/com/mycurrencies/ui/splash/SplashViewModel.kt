package mustafaozhan.github.com.mycurrencies.ui.splash

import io.reactivex.Completable
import mustafaozhan.github.com.mycurrencies.base.BaseViewModel
import mustafaozhan.github.com.mycurrencies.data.repository.PreferencesRepository

class SplashViewModel(
    override val preferencesRepository: PreferencesRepository
) : BaseViewModel() {
    override fun onLoaded(): Completable {
        return Completable.complete()
    }
}
