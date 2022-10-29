package com.oztechan.ccc.test.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.runTest
import kotlin.test.assertTrue

fun <T> SharedFlow<T>.before(
    function: () -> Unit
) = onSubscription {
    function()
}

@Suppress("OPT_IN_USAGE")
fun <T> Flow<T>.after(function: (T?) -> Unit) = runTest {
    delay(100)
    firstOrNull {
        function(it)
        true
    }
}

@Suppress("OPT_IN_USAGE")
fun Any.createTestDispatcher() = newSingleThreadContext(this::class.simpleName.toString())

fun assertAllTrue(vararg condition: Boolean) {
    condition.forEach {
        assertTrue { it }
    }
}
