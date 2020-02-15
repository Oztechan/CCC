@file:Suppress("unused")

package mustafaozhan.github.com.mycurrencies.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
    removeObserver(observer)
    observe(owner, observer)
}

fun <T> Observable<T>.applySchedulers(): Observable<T> =
    observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())

fun <T> Flowable<T>.applySchedulers(): Flowable<T> =
    observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())

fun <T> Single<T>.applySchedulers(): Single<T> =
    observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())

fun Completable.applySchedulers(): Completable =
    observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
