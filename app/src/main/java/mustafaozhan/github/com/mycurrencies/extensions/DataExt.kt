package mustafaozhan.github.com.mycurrencies.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.crashlytics.android.Crashlytics
import com.squareup.moshi.Moshi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import mustafaozhan.github.com.mycurrencies.app.CCCApplication
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.model.CurrencyJson
import mustafaozhan.github.com.mycurrencies.model.Rates
import mustafaozhan.github.com.mycurrencies.room.dao.CurrencyDao

/**
 * Created by Mustafa Ozhan on 2018-07-20.
 */

fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
    removeObserver(observer)
    observe(owner, observer)
}

fun <T> Observable<T>.applySchedulers(): Observable<T> =
    observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())

fun Rates?.calculateResult(name: String, value: String) =
    this?.getThroughReflection<Double>(name)
        ?.times(value.replaceUnsupportedCharacters().toDouble())
        ?: 0.0

inline fun <reified T : Any> Any.getThroughReflection(propertyName: String): T? {
    val getterName = "get" + propertyName.capitalize()
    return try {
        javaClass.getMethod(getterName).invoke(this) as? T
    } catch (e: NoSuchMethodException) {
        e.printStackTrace()
        Crashlytics.logException(e)
        null
    }
}

fun CurrencyDao.insertInitialCurrencies() {
    Moshi.Builder()
        .build()
        .adapter(CurrencyJson::class.java)
        .fromJson(
            CCCApplication.instance.assets.open("currencies.json").bufferedReader()
                .use {
                    it.readText()
                }
        )?.currencies?.forEach { currency ->
        this.insertCurrency(Currency(currency.name, currency.longName, currency.symbol))
    }
}

fun MutableList<Currency>?.removeUnUsedCurrencies(): MutableList<Currency>? =
    this?.filterNot {
        it.name == Currencies.BYR.toString() ||
            it.name == Currencies.LVL.toString() ||
            it.name == Currencies.LTL.toString() ||
            it.name == Currencies.ZMK.toString() ||
            it.name == Currencies.CRYPTO_BTC.toString()
    }?.toMutableList()
