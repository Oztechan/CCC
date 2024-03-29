package com.oztechan.ccc.client.viewmodel.calculator.util

import com.github.submob.scopemob.whetherNot
import com.oztechan.ccc.client.core.shared.util.getRateFromCode
import com.oztechan.ccc.client.core.shared.util.toStandardDigits
import com.oztechan.ccc.client.core.shared.util.toSupportedCharacters
import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.core.model.Currency

internal fun Conversion?.calculateRate(code: String, input: String?) = this
    ?.whetherNot { input.isNullOrEmpty() }
    ?.getRateFromCode(code)
    ?.times(input?.toSupportedCharacters()?.toStandardDigits()?.toDouble() ?: 0.0)
    ?: 0.0

internal fun Currency.getConversionStringFromBase(
    base: String,
    conversion: Conversion?
) = "1 $base = ${conversion?.getRateFromCode(code)} ${getVariablesOneLine()}"

fun List<Currency>?.toValidList(currentBase: String) = this?.filter {
    it.code != currentBase &&
        it.isActive &&
        it.rate != "NaN" &&
        it.rate != "0.0" &&
        it.rate != "0"
} ?: mutableListOf()
