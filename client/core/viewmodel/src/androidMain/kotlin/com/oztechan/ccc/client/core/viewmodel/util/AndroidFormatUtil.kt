package com.oztechan.ccc.client.core.viewmodel.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

actual fun Double.getFormatted(precision: Int): String {
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
