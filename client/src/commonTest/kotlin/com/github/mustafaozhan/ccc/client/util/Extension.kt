package com.github.mustafaozhan.ccc.client.util

import com.github.mustafaozhan.ccc.common.runTest
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.take

fun <T> SharedFlow<T>.test(
    event: () -> Unit,
    assert: (T) -> Unit
) = runTest {
    onSubscription {
        event()
    }.take(1).collect {
        assert(it)
    }
}
