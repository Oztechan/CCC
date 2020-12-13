/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.util

import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.model.Rates
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Suppress("unused")
fun Any?.toUnit() = Unit

fun Long.isWeekPassed(): Boolean {
    return Clock.System.now().toEpochMilliseconds() - this >= WEEK
}

fun Long.isRewardExpired(): Boolean {
    return Clock.System.now().toEpochMilliseconds() - this >= AD_EXPIRATION
}

fun Instant.formatToString() = toLocalDateTime(TimeZone.currentSystemDefault()).run {
    "$hour:$minute $dayOfMonth.$monthNumber.$year"
}

fun CurrencyResponse.toRates(): Rates {
    val rate = rates
    rate.base = base
    rate.date = Clock.System.now().formatToString()
    return rate
}
