package com.github.mustafaozhan.ccc.client.util

import com.github.mustafaozhan.ccc.common.runTest
import com.github.mustafaozhan.ccc.common.util.nowAsInstant
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onSubscription
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlin.random.Random

fun <T> SharedFlow<T>.before(
    function: () -> Unit
) = onSubscription {
    function()
}

fun <T> Flow<T>.after(function: (T?) -> Unit) = runTest {
    delay(100)
    firstOrNull {
        function(it)
        true
    }
}

fun Random.getRandomDateLong(hourRange: Int = 111): Long {
    return if (nextBoolean()) {
        nowAsInstant().plus(nextInt() % hourRange, DateTimeUnit.HOUR).toEpochMilliseconds()
    } else {
        nowAsInstant().minus(nextInt() % hourRange, DateTimeUnit.HOUR).toEpochMilliseconds()
    }
}
