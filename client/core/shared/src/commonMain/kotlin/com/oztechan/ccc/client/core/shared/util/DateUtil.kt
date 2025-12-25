@file:OptIn(ExperimentalTime::class)

package com.oztechan.ccc.client.core.shared.util

import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

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
        "${day.toDoubleDigits()}.${month.number.toDoubleDigits()}.${year.toDoubleDigits()}"
}

private const val BIGGEST_NUMBER = 9

internal fun Int.toDoubleDigits() = if (this <= BIGGEST_NUMBER) "0$this" else "$this"
