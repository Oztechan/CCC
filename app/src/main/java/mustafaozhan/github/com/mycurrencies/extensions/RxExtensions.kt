package mustafaozhan.github.com.mycurrencies.extensions

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:51 PM on Arch Linux wit Love <3.
 */

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
        observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())