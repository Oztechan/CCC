package mustafaozhan.github.com.mycurrencies.extension

import com.squareup.moshi.Moshi
import mustafaozhan.github.com.mycurrencies.app.CCCApplication
import mustafaozhan.github.com.mycurrencies.data.room.currency.CurrencyDao
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.model.CurrencyJson
import mustafaozhan.github.com.mycurrencies.model.Rates

/**
 * Created by Mustafa Ozhan on 2018-07-20.
 */

fun Rates?.calculateResult(name: String, value: String) =
    this?.getThroughReflection<Double>(name)
        ?.times(value.replaceUnsupportedCharacters().toDouble())
        ?: 0.0

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
