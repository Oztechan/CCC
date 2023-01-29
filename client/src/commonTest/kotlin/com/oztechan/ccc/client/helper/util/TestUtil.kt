package com.oztechan.ccc.client.helper.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.test.runTest

fun <T> SharedFlow<T>.before(
    function: () -> Unit
) = onSubscription {
    function()
}

@Suppress("OPT_IN_USAGE")
fun <T> Flow<T>.after(function: (T?) -> Unit) = runTest {
    firstOrNull {
        function(it)
        true
    }
}
