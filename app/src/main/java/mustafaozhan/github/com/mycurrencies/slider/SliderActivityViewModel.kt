package mustafaozhan.github.com.mycurrencies.slider

import io.reactivex.Completable
import mustafaozhan.github.com.mycurrencies.base.BaseViewModel
import mustafaozhan.github.com.mycurrencies.tools.DataManager

class SliderActivityViewModel(dataManager: DataManager) : BaseViewModel(dataManager) {
    override fun onLoaded(): Completable {
        return Completable.complete()
    }
}
