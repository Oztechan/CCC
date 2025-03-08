package com.oztechan.ccc.client.viewmodel.premium.util

import com.oztechan.ccc.client.core.shared.util.nowAsLong
import com.oztechan.ccc.client.core.shared.util.toInstant
import com.oztechan.ccc.client.viewmodel.premium.model.PremiumType
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus

internal const val REWARDED_AD_PREMIUM_IN_DAYS = 2

fun PremiumType.calculatePremiumEnd(startDate: Long = nowAsLong()) = when (this) {
    PremiumType.VIDEO -> startDate.toInstant().plus(
        value = REWARDED_AD_PREMIUM_IN_DAYS,
        DateTimeUnit.DAY,
        TimeZone.currentSystemDefault()
    ).toEpochMilliseconds()

    PremiumType.MONTH -> startDate.toInstant().plus(
        value = 1,
        DateTimeUnit.MONTH,
        TimeZone.currentSystemDefault()
    ).toEpochMilliseconds()

    PremiumType.QUARTER -> startDate.toInstant().plus(
        value = 3,
        DateTimeUnit.MONTH,
        TimeZone.currentSystemDefault()
    ).toEpochMilliseconds()

    PremiumType.HALF_YEAR -> startDate.toInstant().plus(
        value = 6,
        DateTimeUnit.MONTH,
        TimeZone.currentSystemDefault()
    ).toEpochMilliseconds()

    PremiumType.YEAR -> startDate.toInstant().plus(
        value = 1,
        DateTimeUnit.YEAR,
        TimeZone.currentSystemDefault()
    ).toEpochMilliseconds()

    PremiumType.LIFE_TIME -> startDate.toInstant().plus(
        value = 1,
        DateTimeUnit.CENTURY,
        TimeZone.currentSystemDefault()
    ).toEpochMilliseconds()
}
