package mustafaozhan.github.com.mycurrencies.base

import androidx.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import mustafaozhan.github.com.mycurrencies.extensions.applySchedulers
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.MainData
import mustafaozhan.github.com.mycurrencies.tools.DataManager
import org.joda.time.Duration
import org.joda.time.Instant

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:40 PM on Arch Linux wit Love <3.
 */
abstract class BaseViewModel : ViewModel() {

    companion object {
        const val NUMBER_OF_HOURS = 24
    }

    abstract val dataManager: DataManager
    private val compositeDisposable by lazy { CompositeDisposable() }
    lateinit var mainData: MainData

    protected fun <T> subscribeService(
        serviceObservable: Observable<T>,
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) = compositeDisposable.add(
        serviceObservable.applySchedulers().subscribe(onNext, onError)
    )

    abstract fun onLoaded(): Completable

    override fun onCleared() {
        compositeDisposable.clear()
        compositeDisposable.dispose()
        super.onCleared()
    }

    open fun setCurrentBase(newBase: String?) {
        mainData.currentBase = Currencies.valueOf(newBase ?: "NULL")
        dataManager.persistMainData(mainData)
    }

    open fun savePreferences() = dataManager.persistMainData(mainData)

    protected fun loadPreferences() {
        mainData = dataManager.loadMainData()
    }

    open fun isSliderShown() = dataManager.loadMainData().sliderShown

    open fun setSliderShown() {
        loadPreferences()
        mainData.sliderShown = true
        savePreferences()
    }

    open fun isRewardExpired() = dataManager.loadMainData().adFreeActivatedDate?.let {
        Duration(it, Instant.now()).standardHours > NUMBER_OF_HOURS
    } ?: true
}
