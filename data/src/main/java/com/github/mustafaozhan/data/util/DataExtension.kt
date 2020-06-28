/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.data.util

import com.github.mustafaozhan.data.model.Currencies
import com.github.mustafaozhan.data.model.Currency
import com.github.mustafaozhan.data.model.Rates
import com.github.mustafaozhan.scopemob.whetherNot

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
