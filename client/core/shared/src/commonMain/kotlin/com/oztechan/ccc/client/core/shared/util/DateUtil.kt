package com.oztechan.ccc.client.core.shared.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun nowAsLong() = nowAsInstant().toEpochMilliseconds()

fun nowAsInstant() = Clock.System.now()

fun Long.isItOver(): Boolean {
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

private const val BIGGEST_DIGIT = 9

internal fun Int.toDoubleDigits() = if (this <= BIGGEST_DIGIT) "0$this" else "$this"
