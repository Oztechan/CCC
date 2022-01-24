package com.github.mustafaozhan.ccc.common.util

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ResultTest {

    @Test
    fun success_and_complete_is_invoked() {
        val mock = "mock"
        var completeInvoked = false

        Result.Success(mock).execute(
            success = { assertEquals(mock, it) },
            error = { assertTrue { false } },
            complete = { completeInvoked = true }
        )

        assertTrue { completeInvoked }
    }

    @Test
    fun error_and_complete_is_invoked() {
        val mockThrowable = Throwable("mock")
        var completeInvoked = false

        Result.Error(mockThrowable).execute(
            success = { assertTrue { false } },
            error = { assertEquals(mockThrowable, it) },
            complete = { completeInvoked = true }
        )

        assertTrue { completeInvoked }
    }
}
