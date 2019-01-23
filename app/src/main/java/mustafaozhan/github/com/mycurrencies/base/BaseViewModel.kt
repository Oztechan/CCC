package mustafaozhan.github.com.mycurrencies.base

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import mustafaozhan.github.com.mycurrencies.app.Application
import mustafaozhan.github.com.mycurrencies.dagger.component.ViewModelComponent
import mustafaozhan.github.com.mycurrencies.extensions.applySchedulers
import mustafaozhan.github.com.mycurrencies.tools.DataManager
import javax.inject.Inject


/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:40 PM on Arch Linux wit Love <3.
 */
abstract class BaseViewModel : ViewModel() {

    protected val viewModelComponent: ViewModelComponent by lazy { Application.instance.component.viewModelComponent() }
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    @Inject
    lateinit var dataManager: DataManager

    init {
        inject()
    }

    protected abstract fun inject()

    protected fun <T> subscribeService(serviceObservable: Observable<T>, onNext: (T) -> Unit,
                                       onError: (Throwable) -> Unit, onComplete: () -> Unit) {
        compositeDisposable.add(serviceObservable.applySchedulers().subscribe(onNext, onError, onComplete))
    }

    protected fun <T> subscribeService(serviceObservable: Observable<T>, onNext: (T) -> Unit, onError: (Throwable) -> Unit) {
        compositeDisposable.add(serviceObservable.applySchedulers().subscribe(onNext, onError))
    }

    protected fun <T> subscribeService(serviceSingle: Single<T>, onSuccess: (T) -> Unit, onError: (Throwable) -> Unit) {
        compositeDisposable.add(serviceSingle.applySchedulers().subscribe(onSuccess, onError))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}