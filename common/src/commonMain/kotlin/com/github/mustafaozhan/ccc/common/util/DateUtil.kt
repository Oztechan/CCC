package com.github.mustafaozhan.ccc.common.util

import kotlinx.datetime.Clock

fun nowAsLong() = nowAsInstant().toEpochMilliseconds()

fun nowAsInstant() = Clock.System.now()
