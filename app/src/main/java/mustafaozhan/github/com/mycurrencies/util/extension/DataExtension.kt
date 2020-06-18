/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.util.extension

import com.github.mustafaozhan.scopemob.whetherNot
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.model.Rates

fun Rates?.calculateResult(name: String, value: String?) =
    this?.whetherNot { value.isNullOrEmpty() }
        ?.getThroughReflection<Double>(name)
        ?.times(value?.toSupportedCharacters()?.toStandardDigits()?.toDouble() ?: 0.0)
        ?: 0.0

fun Currency.getCurrencyConversionByRate(base: String, rate: Rates?) =
    "1 $base = " + "${rate?.getThroughReflection<Double>(name)} ${getVariablesOneLine()}"

fun MutableList<Currency>?.removeUnUsedCurrencies(): MutableList<Currency>? =
    this?.filterNot { (name) ->
        name == Currencies.BYR.toString() ||
            name == Currencies.LVL.toString() ||
            name == Currencies.LTL.toString() ||
            name == Currencies.ZMK.toString() ||
            name == Currencies.CRYPTO_BTC.toString()
    }?.toMutableList()

fun MutableList<Currency>?.toValidList(currentBase: String) =
    this?.filter {
        it.name != currentBase &&
            it.isActive &&
            it.rate.toString() != "NaN" &&
            it.rate.toString() != "0.0"
    } ?: mutableListOf()
