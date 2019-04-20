package mustafaozhan.github.com.mycurrencies.base

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import mustafaozhan.github.com.mycurrencies.app.Application
import mustafaozhan.github.com.mycurrencies.dagger.component.ViewModelComponent
import mustafaozhan.github.com.mycurrencies.extensions.applySchedulers
import mustafaozhan.github.com.mycurrencies.model.MainData
import mustafaozhan.github.com.mycurrencies.tools.Currencies
import mustafaozhan.github.com.mycurrencies.tools.DataManager
import org.joda.time.Duration
import org.joda.time.Instant
import javax.inject.Inject

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:40 PM on Arch Linux wit Love <3.
 */
abstract class BaseViewModel : ViewModel() {

    companion object {
        const val NUMBER_OF_DAYS = 3
    }

    protected val viewModelComponent: ViewModelComponent by lazy { Application.instance.component.viewModelComponent() }
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    lateinit var mainData: MainData

    @Inject
    lateinit var dataManager: DataManager

    init {
        inject()
    }

    protected abstract fun inject()

    protected fun <T> subscribeService(
        serviceObservable: Observable<T>,
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit,
        onComplete: () -> Unit
    ) = compositeDisposable.add(serviceObservable
        .applySchedulers()
        .subscribe(onNext, onError, onComplete)
    )

    protected fun <T> subscribeService(
        serviceObservable: Observable<T>,
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) = compositeDisposable.add(
        serviceObservable.applySchedulers().subscribe(onNext, onError)
    )

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun setCurrentBase(newBase: String?) {
        mainData.currentBase = Currencies.valueOf(newBase ?: "NULL")
        dataManager.persistMainData(mainData)
    }

    fun savePreferences() = dataManager.persistMainData(mainData)

    fun isRewardExpired() = !(mainData.adFreeActivatedDate != null &&
        Duration(mainData.adFreeActivatedDate, Instant.now()).standardDays <= NUMBER_OF_DAYS)

    protected fun loadPreferences() {
        mainData = dataManager.loadMainData()
    }
}