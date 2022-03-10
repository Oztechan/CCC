package com.oztechan.ccc.client.util

import com.github.mustafaozhan.ccc.common.util.nowAsLong
import com.oztechan.ccc.client.model.RemoveAdType
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

const val VIDEO_REWARD = 2
private const val BIGGEST_DIGIT = 9

fun Long.isRewardExpired(): Boolean {
    return nowAsLong() >= this
}

fun Long.toInstant() = Instant.fromEpochMilliseconds(this)

fun Long.toDateString(
    timeZone: TimeZone = TimeZone.currentSystemDefault()
) = toInstant().toDateString(timeZone)

fun Instant.toDateString(
    timeZone: TimeZone = TimeZone.currentSystemDefault()
) = toLocalDateTime(timeZone).run {
    "${hour.toDoubleDigits()}:${minute.toDoubleDigits()} " +
        "${dayOfMonth.toDoubleDigits()}.${monthNumber.toDoubleDigits()}.${year.toDoubleDigits()}"
}

fun Int.toDoubleDigits() = if (this <= BIGGEST_DIGIT) "0$this" else "$this"

@Suppress("MagicNumber")
fun RemoveAdType.calculateAdRewardEnd(startDate: Long = nowAsLong()) = when (this) {
    RemoveAdType.VIDEO -> startDate.toInstant().plus(
        VIDEO_REWARD,
        DateTimeUnit.DAY,
        TimeZone.currentSystemDefault()
    ).toEpochMilliseconds()
    RemoveAdType.MONTH -> startDate.toInstant().plus(
        1,
        DateTimeUnit.MONTH,
        TimeZone.currentSystemDefault()
    ).toEpochMilliseconds()
    RemoveAdType.QUARTER -> startDate.toInstant().plus(
        3,
        DateTimeUnit.MONTH,
        TimeZone.currentSystemDefault()
    ).toEpochMilliseconds()
    RemoveAdType.HALF_YEAR -> startDate.toInstant().plus(
        6,
        DateTimeUnit.MONTH,
        TimeZone.currentSystemDefault()
    ).toEpochMilliseconds()
    RemoveAdType.YEAR -> startDate.toInstant().plus(
        1,
        DateTimeUnit.YEAR,
        TimeZone.currentSystemDefault()
    ).toEpochMilliseconds()
    RemoveAdType.LIFE_TIME -> startDate.toInstant().plus(
        1,
        DateTimeUnit.CENTURY,
        TimeZone.currentSystemDefault()
    ).toEpochMilliseconds()
}
