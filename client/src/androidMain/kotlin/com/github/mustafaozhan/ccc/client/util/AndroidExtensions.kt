/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

private const val MAXIMUM_FLOATING_POINT = 15

actual fun Double.getFormatted(): String {

    var decimalFormat = "###,###.###"
    val symbols = DecimalFormatSymbols.getInstance()
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
