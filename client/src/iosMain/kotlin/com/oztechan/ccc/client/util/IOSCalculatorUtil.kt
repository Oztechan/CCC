/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.util

import com.oztechan.ccc.client.viewmodel.notification.NotificationData
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle

actual fun Double.getFormatted() = NSNumberFormatter().apply {
    setNumberStyle(NSNumberFormatterDecimalStyle)
    setGroupingSeparator(" ")
    setDecimalSeparator(".")
}.stringFromNumber(NSNumber(this)) ?: ""

actual fun Double.removeScientificNotation() = NSNumberFormatter().apply {
    setNumberStyle(NSNumberFormatterDecimalStyle)
    setGroupingSeparator("")
    setDecimalSeparator(".")
    setMaximumFractionDigits(NotificationData.MAXIMUM_INPUT.toULong())
}.stringFromNumber(NSNumber(this)) ?: ""
