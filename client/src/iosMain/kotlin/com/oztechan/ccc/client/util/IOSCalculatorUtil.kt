/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.util

import com.oztechan.ccc.client.viewmodel.watchers.WatchersData
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle

actual fun Double.getFormatted(precision: Int) = NSNumberFormatter().apply {
    var currentPrecision = precision.toULong()
    setNumberStyle(NSNumberFormatterDecimalStyle)
    setGroupingSeparator(" ")
    setDecimalSeparator(".")
    setMaximumFractionDigits(currentPrecision)
    // increasing floating digits for too small numbers
    repeat(MAXIMUM_FLOATING_POINT - precision + 1) {
        if (stringFromNumber(NSNumber(this@getFormatted)).orEmpty() == "0") {
            currentPrecision = precision.toULong() + it.toULong()
            setMaximumFractionDigits(currentPrecision)
        }
    }
}.stringFromNumber(NSNumber(this)).orEmpty()

actual fun Double.removeScientificNotation() = NSNumberFormatter().apply {
    setNumberStyle(NSNumberFormatterDecimalStyle)
    setGroupingSeparator("")
    setDecimalSeparator(".")
    setMaximumFractionDigits(WatchersData.MAXIMUM_INPUT.toULong())
}.stringFromNumber(NSNumber(this)).orEmpty()
