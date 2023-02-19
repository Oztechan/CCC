package com.oztechan.ccc.client.core.shared.util

import com.oztechan.ccc.client.core.shared.model.PremiumType
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus

internal const val REWARDED_AD_PREMIUM_IN_DAYS = 2

@Suppress("MagicNumber")
fun PremiumType.calculatePremiumEnd(startDate: Long = nowAsLong()) = when (this) {
    PremiumType.VIDEO -> startDate.toInstant().plus(
        REWARDED_AD_PREMIUM_IN_DAYS,
        DateTimeUnit.DAY,
        TimeZone.currentSystemDefault()
    ).toEpochMilliseconds()

    PremiumType.MONTH -> startDate.toInstant().plus(
        1,
        DateTimeUnit.MONTH,
        TimeZone.currentSystemDefault()
    ).toEpochMilliseconds()

    PremiumType.QUARTER -> startDate.toInstant().plus(
        3,
        DateTimeUnit.MONTH,
        TimeZone.currentSystemDefault()
    ).toEpochMilliseconds()

    PremiumType.HALF_YEAR -> startDate.toInstant().plus(
        6,
        DateTimeUnit.MONTH,
        TimeZone.currentSystemDefault()
    ).toEpochMilliseconds()

    PremiumType.YEAR -> startDate.toInstant().plus(
        1,
        DateTimeUnit.YEAR,
        TimeZone.currentSystemDefault()
    ).toEpochMilliseconds()

    PremiumType.LIFE_TIME -> startDate.toInstant().plus(
        1,
        DateTimeUnit.CENTURY,
        TimeZone.currentSystemDefault()
    ).toEpochMilliseconds()
}
