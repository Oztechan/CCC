/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.extension

import android.content.Context
import com.github.mustafaozhan.scopemob.whetherNot
import com.squareup.moshi.Moshi
import mustafaozhan.github.com.mycurrencies.data.room.currency.CurrencyDao
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.model.CurrencyJson
import mustafaozhan.github.com.mycurrencies.model.Rates

fun Rates?.calculateResult(name: String, value: String?) =
    this?.whetherNot { value.isNullOrEmpty() }
        ?.getThroughReflection<Double>(name)
        ?.times(value?.toSupportedCharacters()?.toStandardDigits()?.toDouble() ?: 0.0)
        ?: 0.0

fun CurrencyDao.insertInitialCurrencies(context: Context) {
    Moshi.Builder()
        .build()
        .adapter(CurrencyJson::class.java)
        .fromJson(
            context.assets.open("currencies.json").bufferedReader()
                .use { it.readText() }
        )?.currencies?.forEach { (name, longName, symbol) ->
            this.insertCurrency(Currency(name, longName, symbol))
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

fun MutableList<Currency>?.toValidList(currentBase: String) =
    this?.filter {
        it.name != currentBase &&
            it.isActive &&
            it.rate.toString() != "NaN" &&
            it.rate.toString() != "0.0"
    } ?: mutableListOf()
