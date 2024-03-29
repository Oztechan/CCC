package com.oztechan.ccc.common.core.network.base

import com.oztechan.ccc.common.core.network.error.EmptyParameterException
import com.oztechan.ccc.common.core.network.error.ModelMappingException
import com.oztechan.ccc.common.core.network.error.NetworkException
import com.oztechan.ccc.common.core.network.error.TerminationException
import com.oztechan.ccc.common.core.network.error.TimeoutException
import com.oztechan.ccc.common.core.network.error.UnknownNetworkException
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.Test
import kotlin.test.assertFailsWith

internal class BaseNetworkServiceTest {

    @Suppress("OPT_IN_USAGE")
    private val subject = object : BaseNetworkService(UnconfinedTestDispatcher()) {
        fun parameterCheck(parameter: String) = withEmptyParameterCheck(parameter)
        suspend fun <T> request(
            suspendBlock: suspend () -> T
        ) = apiRequest { suspendBlock.invoke() }
    }

    private val exception = Exception("Test exception")

    @Test
    fun `CancellationException should return exception itself`() = runTest {
        assertFailsWith(TerminationException::class) {
            subject.request { throw CancellationException(exception) }
        }
    }

    @Test
    fun `IOException should return NetworkException`() = runTest {
        assertFailsWith(NetworkException::class) {
            subject.request { throw IOException(exception.message.toString()) }
        }
    }

    @Test
    fun `ConnectTimeoutException should return TimeoutException`() = runTest {
        assertFailsWith(TimeoutException::class) {
            subject.request { throw ConnectTimeoutException(exception.message.toString()) }
        }
    }

    @Test
    fun `SerializationException should return ModelMappingException`() = runTest {
        assertFailsWith(ModelMappingException::class) {
            subject.request { throw SerializationException(exception) }
        }
    }

    @Test
    fun `Any other exception should return UnknownNetworkException`() = runTest {
        assertFailsWith(UnknownNetworkException::class) {
            subject.request { throw exception }
        }
    }

    @Test
    fun `Empty string should return EmptyParameterException`() {
        assertFailsWith(EmptyParameterException::class) {
            subject.parameterCheck("")
        }
    }
}
