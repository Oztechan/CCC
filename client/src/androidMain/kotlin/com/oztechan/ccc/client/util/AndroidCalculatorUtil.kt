/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.util

import com.oztechan.ccc.client.viewmodel.watchers.WatchersData
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

private const val MAXIMUM_FLOATING_POINT = 15

actual fun Double.getFormatted(): String {

    var decimalFormat = "###,###.###"
    val symbols = DecimalFormatSymbols.getInstance(Locale.ENGLISH)
    symbols.groupingSeparator = ' '

    // increasing floating digits for too small numbers
    repeat(MAXIMUM_FLOATING_POINT) {
        if (DecimalFormat(decimalFormat, symbols).format(this) == "0") {
            decimalFormat = "$decimalFormat#"
        }
    }
    decimalFormat = "$decimalFormat#"
    return DecimalFormat(decimalFormat, symbols).format(this)
}

actual fun Double.removeScientificNotation() = DecimalFormat("#.#").apply {
    maximumFractionDigits = WatchersData.MAXIMUM_INPUT
    decimalFormatSymbols = DecimalFormatSymbols.getInstance(Locale.ENGLISH)
}.format(this)
