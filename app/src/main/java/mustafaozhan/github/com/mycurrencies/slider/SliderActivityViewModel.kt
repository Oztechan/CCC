package mustafaozhan.github.com.mycurrencies.slider

import io.reactivex.Completable
import mustafaozhan.github.com.mycurrencies.base.BaseViewModel

class SliderActivityViewModel : BaseViewModel() {
    override fun onLoaded(): Completable {
        return Completable.complete()
    }
}
