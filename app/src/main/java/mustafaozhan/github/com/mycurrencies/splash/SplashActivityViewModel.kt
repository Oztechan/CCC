package mustafaozhan.github.com.mycurrencies.splash

import io.reactivex.Completable
import mustafaozhan.github.com.mycurrencies.base.BaseViewModel
import mustafaozhan.github.com.mycurrencies.repository.old.DataManager

class SplashActivityViewModel(
    override val dataManager: DataManager
) : BaseViewModel() {
    override fun onLoaded(): Completable {
        return Completable.complete()
    }
}
