/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.util

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter

actual fun Double.getFormatted(): String {
    val formatter = NSNumberFormatter()
    formatter.setNumberStyle(platform.Foundation.NSNumberFormatterDecimalStyle)
    return formatter.stringFromNumber(NSNumber(this))?.replace(",", " ") ?: ""
}
