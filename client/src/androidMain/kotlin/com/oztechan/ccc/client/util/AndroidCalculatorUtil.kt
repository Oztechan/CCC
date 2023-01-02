/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.util

import com.oztechan.ccc.client.viewmodel.watchers.WatchersData
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

internal actual fun Double.getFormatted(precision: Int): String {
    var decimalFormat = "###,###."
    repeat(precision) {
        decimalFormat = "$decimalFormat#"
    }

    val symbols = DecimalFormatSymbols.getInstance(Locale.ENGLISH)
    symbols.groupingSeparator = ' '

    // increasing floating digits for too small numbers
    repeat(MAXIMUM_FLOATING_POINT - precision) {
        if (DecimalFormat(decimalFormat, symbols).format(this) == "0") {
            decimalFormat = "$decimalFormat#"
        }
    }
    return DecimalFormat(decimalFormat, symbols).format(this)
}

internal actual fun Double.removeScientificNotation(): String = DecimalFormat("#.#").apply {
    maximumFractionDigits = WatchersData.MAXIMUM_INPUT
    decimalFormatSymbols = DecimalFormatSymbols.getInstance(Locale.ENGLISH)
}.format(this)
