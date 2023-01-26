package com.oztechan.ccc.client.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal fun nowAsLong() = nowAsInstant().toEpochMilliseconds()

internal fun nowAsInstant() = Clock.System.now()

internal fun Long.isItOver(): Boolean {
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

private const val BIGGEST_DIGIT = 9

internal fun Int.toDoubleDigits() = if (this <= BIGGEST_DIGIT) "0$this" else "$this"
