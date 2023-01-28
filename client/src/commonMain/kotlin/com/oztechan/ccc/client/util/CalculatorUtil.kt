package com.oztechan.ccc.client.util

import com.github.submob.scopemob.whether
import com.github.submob.scopemob.whetherNot
import com.oztechan.ccc.client.core.shared.util.getRateFromCode
import com.oztechan.ccc.client.viewmodel.util.toSupportedCharacters
import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.core.model.Currency

internal fun Conversion?.calculateRate(code: String, input: String?) = this
    ?.whetherNot { input.isNullOrEmpty() }
    ?.getRateFromCode(code)
    ?.times(input?.toSupportedCharacters()?.toStandardDigits()?.toDouble() ?: 0.0)
    ?: 0.0

internal fun String.toStandardDigits(): String {
    val builder = StringBuilder()
    forEach { char ->
        char.toString().toIntOrNull()
            ?.whether { it >= 0 }
            ?.let { builder.append(it) }
            ?: run { builder.append(char) }
    }
    return builder.toString()
}

internal fun Currency.getConversionStringFromBase(
    base: String,
    conversion: Conversion?
) = "1 $base = ${conversion?.getRateFromCode(code)} ${getVariablesOneLine()}"

fun List<Currency>?.toValidList(currentBase: String) = this?.filter {
    it.code != currentBase &&
        it.isActive &&
        it.rate.toString() != "NaN" &&
        it.rate.toString() != "0.0" &&
        it.rate.toString() != "0"
} ?: mutableListOf()

internal fun Int.indexToNumber() = this + 1

fun Int.numberToIndex() = this - 1
