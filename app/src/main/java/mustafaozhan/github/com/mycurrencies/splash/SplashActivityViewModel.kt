package mustafaozhan.github.com.mycurrencies.splash

import io.reactivex.Completable
import mustafaozhan.github.com.mycurrencies.base.BaseViewModel

class SplashActivityViewModel : BaseViewModel() {
    override fun onLoaded(): Completable {
        return Completable.complete()
    }
}
