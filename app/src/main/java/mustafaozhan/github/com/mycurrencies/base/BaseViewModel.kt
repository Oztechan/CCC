package mustafaozhan.github.com.mycurrencies.base

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import mustafaozhan.github.com.mycurrencies.app.Application
import mustafaozhan.github.com.mycurrencies.dagger.component.ViewModelComponent
import mustafaozhan.github.com.mycurrencies.extensions.applySchedulers
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.MainData
import mustafaozhan.github.com.mycurrencies.tools.DataManager
import javax.inject.Inject

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:40 PM on Arch Linux wit Love <3.
 */
abstract class BaseViewModel : ViewModel() {

    companion object {
        const val NUMBER_OF_HOURS = 3
    }

    protected val viewModelComponent: ViewModelComponent by lazy { Application.instance.component.viewModelComponent() }
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    lateinit var mainData: MainData

    @Inject
    lateinit var dataManager: DataManager

    init {
        @Suppress("LeakingThis")
        inject()
    }

    protected abstract fun inject()

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
}
