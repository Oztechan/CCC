package com.oztechan.ccc.client.util

import com.oztechan.ccc.client.model.PremiumType
import com.oztechan.ccc.common.util.nowAsLong
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

internal const val REWARDED_AD_PREMIUM_IN_DAYS = 2
private const val BIGGEST_DIGIT = 9

internal fun Long.isPremiumExpired(): Boolean {
    return nowAsLong() >= this
}

internal fun Long.toInstant() = Instant.fromEpochMilliseconds(this)

internal fun Long.toDateString(
    timeZone: TimeZone = TimeZone.currentSystemDefault()
) = toInstant().toDateString(timeZone)

internal fun Instant.toDateString(
    timeZone: TimeZone = TimeZone.currentSystemDefault()
) = toLocalDateTime(timeZone).run {
    "${hour.toDoubleDigits()}:${minute.toDoubleDigits()} " +
        "${dayOfMonth.toDoubleDigits()}.${monthNumber.toDoubleDigits()}.${year.toDoubleDigits()}"
}

internal fun Int.toDoubleDigits() = if (this <= BIGGEST_DIGIT) "0$this" else "$this"

@Suppress("MagicNumber")
internal fun PremiumType.calculatePremiumEnd(startDate: Long = nowAsLong()) = when (this) {
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
