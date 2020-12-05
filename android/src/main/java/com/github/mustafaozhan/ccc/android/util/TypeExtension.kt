/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.util

import android.annotation.SuppressLint
import com.github.mustafaozhan.ccc.common.kermit
import com.github.mustafaozhan.ccc.common.model.CurrencyResponseV2
import com.github.mustafaozhan.ccc.common.model.RatesV2
import com.github.mustafaozhan.data.model.CurrencyResponse
import com.github.mustafaozhan.data.model.Rates
import com.github.mustafaozhan.scopemob.mapTo
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.scopemob.whetherNot
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val DATE_FORMAT = "HH:mm dd.MM.yyyy"
private const val MAXIMUM_FLOATING_POINT = 15

// todo check append
fun String.toStandardDigits(): String {
    val builder = StringBuilder()
    forEach { char ->
        char.whether { Character.isDigit(it) }
            ?.whetherNot { it in '0'..'9' }
            ?.mapTo { Character.getNumericValue(it) }
            ?.whether { it >= 0 }
            ?.let { builder.append(it) }
            ?: run { builder.append(char) }
    }
    return builder.toString()
}

fun String.toSupportedCharacters() =
    replace(",", ".")
        .replace("٫", ".")
        .replace(" ", "")
        .replace("−", "-")

fun String.toPercent() = replace("%", "/100*")

fun Double.getFormatted(): String {

    var decimalFormat = "###,###.###"
    val symbols = DecimalFormatSymbols.getInstance()
    symbols.groupingSeparator = ' '

    // increasing floating digits for too small numbers
    for (i in 0..MAXIMUM_FLOATING_POINT) {
        if (DecimalFormat(decimalFormat, symbols).format(this) == "0") {
            decimalFormat = "$decimalFormat#"
        }
    }
    decimalFormat = "$decimalFormat#"
    return DecimalFormat(decimalFormat, symbols).format(this)
}

fun String.dropDecimal() = replace(" ", "").let { nonEmpty ->
    nonEmpty.whether { contains(".") }
        ?.substring(0, nonEmpty.indexOf("."))
        ?: run { nonEmpty }
}

fun Date.dateStringToFormattedString(): String =
    SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(this)

fun CurrencyResponse.toRate(): Rates {
    val rate = rates
    rate.base = base
    // todo need to change to CurrencyResponse.date when BE return date
    rate.date = Date().dateStringToFormattedString()
    return rate
}

fun CurrencyResponseV2.toRateV2(): RatesV2 {
    val rate = rates
    rate.base = base
    // todo need to change to CurrencyResponse.date when BE return date
    rate.date = Date().dateStringToFormattedString()
    return rate
}

@SuppressLint("DefaultLocale")
inline fun <reified T> Any.getThroughReflection(propertyName: String): T? {
    val getterName = "get" + propertyName.capitalize()
    return try {
        javaClass.getMethod(getterName).invoke(this) as? T
    } catch (e: NoSuchMethodException) {
        kermit.e(e) { e.message.toString() }
        null
    }
}
