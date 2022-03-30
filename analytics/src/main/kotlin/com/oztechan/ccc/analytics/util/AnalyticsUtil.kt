package com.oztechan.ccc.analytics.util

import android.content.Context
import com.scottyab.rootbeer.RootBeer

fun isDeviceRooted(context: Context) = if (RootBeer(context).isRooted) {
    true.toString()
} else {
    false.toString()
}
