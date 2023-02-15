package com.oztechan.ccc.client.core.shared.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal fun nowAsInstant() = Clock.System.now()

fun nowAsLong() = nowAsInstant().toEpochMilliseconds()

fun nowAsDateString() = nowAsInstant().toDateString()

fun Long.isPassed(): Boolean {
    return nowAsLong() >= this
}

fun Long.isNotPassed(): Boolean = !isPassed()

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

private const val BIGGEST_NUMBER = 9

internal fun Int.toDoubleDigits() = if (this <= BIGGEST_NUMBER) "0$this" else "$this"
