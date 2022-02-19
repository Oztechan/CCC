package com.github.mustafaozhan.ccc.common.util

import kotlinx.datetime.Clock

const val SECOND: Long = 1000L
const val DAY: Long = 24 * 60 * 60 * SECOND

fun nowAsLong() = nowAsInstant().toEpochMilliseconds()

fun nowAsInstant() = Clock.System.now()
