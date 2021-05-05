package com.github.mustafaozhan.ccc.client.util

import com.github.mustafaozhan.ccc.common.runTest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onSubscription

fun <T> SharedFlow<T>.before(
    function: () -> Unit
) = onSubscription {
    function()
}

fun <T> Flow<T>.after(function: (T?) -> Unit) = runTest {
    firstOrNull {
        function(it)
        true
    }
}
