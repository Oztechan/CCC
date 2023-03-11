package com.oztechan.ccc.common.core.network.base

import com.oztechan.ccc.common.core.network.error.EmptyParameterException
import com.oztechan.ccc.common.core.network.error.ModelMappingException
import com.oztechan.ccc.common.core.network.error.NetworkException
import com.oztechan.ccc.common.core.network.error.TerminationException
import com.oztechan.ccc.common.core.network.error.TimeoutException
import com.oztechan.ccc.common.core.network.error.UnknownNetworkException
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

@Suppress("OPT_IN_USAGE")
class BaseNetworkServiceTest {

    private val subject = object : BaseNetworkService(newSingleThreadContext(this::class.simpleName.toString())) {
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
        }.let {
            assertNotNull(it.cause)
            assertNotNull(it.cause.cause)
            assertEquals(exception, it.cause.cause)
            assertEquals(exception.message, it.cause.cause!!.message)
        }
    }

    @Test
    fun `IOException should return NetworkException`() = runTest {
        assertFailsWith(NetworkException::class) {
            subject.request { throw IOException(exception.message.toString()) }
        }.let {
            assertNotNull(it.cause)
            assertEquals(exception.message, it.cause.message)
        }
    }

    @Test
    fun `ConnectTimeoutException should return TimeoutException`() = runTest {
        assertFailsWith(TimeoutException::class) {
            subject.request { throw ConnectTimeoutException(exception.message.toString()) }
        }.let {
            assertNotNull(it.cause)
            assertEquals(exception.message, it.cause.message)
        }
    }

    @Test
    fun `SerializationException should return ModelMappingException`() = runTest {
        assertFailsWith(ModelMappingException::class) {
            subject.request { throw SerializationException(exception) }
        }.let {
            assertNotNull(it.cause)
            assertNotNull(it.cause.cause)
            assertEquals(exception, it.cause.cause)
            assertEquals(exception.message, it.cause.cause!!.message)
        }
    }

    @Suppress("TooGenericExceptionThrown")
    @Test
    fun `Any other exception should return UnknownNetworkException`() = runTest {
        assertFailsWith(UnknownNetworkException::class) {
            subject.request { throw Exception(exception) }
        }.let {
            assertNotNull(it.cause)
            assertNotNull(it.cause.cause)
            assertEquals(exception, it.cause.cause)
            assertEquals(exception.message, it.cause.cause!!.message)
        }
    }

    @Test
    fun `Empty string should return EmptyParameterException`() = runTest {
        assertFailsWith(EmptyParameterException::class) {
            subject.parameterCheck("")
        }.let {
            assertNotNull(it)
            assertEquals(EmptyParameterException().message, it.message)
        }
    }
}
