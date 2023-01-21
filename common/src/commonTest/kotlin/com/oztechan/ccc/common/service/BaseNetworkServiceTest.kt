package com.oztechan.ccc.common.service

import com.oztechan.ccc.common.core.network.api.free.FreeApi
import com.oztechan.ccc.common.core.network.error.ModelMappingException
import com.oztechan.ccc.common.core.network.error.NetworkException
import com.oztechan.ccc.common.core.network.error.TerminationException
import com.oztechan.ccc.common.core.network.error.TimeoutException
import com.oztechan.ccc.common.core.network.error.UnknownNetworkException
import com.oztechan.ccc.common.service.free.FreeApiService
import com.oztechan.ccc.common.service.free.FreeApiServiceImpl
import com.oztechan.ccc.test.BaseSubjectTest
import com.oztechan.ccc.test.util.createTestDispatcher
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.utils.io.errors.IOException
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

@Suppress("OPT_IN_USAGE")
class BaseNetworkServiceTest : BaseSubjectTest<FreeApiService>() {
    override val subject: FreeApiService by lazy {
        FreeApiServiceImpl(freeApi, createTestDispatcher())
    }

    @Mock
    private val freeApi = mock(classOf<FreeApi>())

    private val base = "EUR"

    private val exception = Exception("Test exception")

    @Test
    fun `CancellationException should return exception itself`() = runTest {
        given(freeApi)
            .coroutine { getConversion(base) }
            .thenThrow(CancellationException(exception))

        assertFailsWith(TerminationException::class) {
            subject.getConversion(base)
        }.let {
            assertNotNull(it.cause)
            assertNotNull(it.cause!!.cause)
            assertEquals(exception, it.cause!!.cause)
            assertEquals(exception.message, it.cause!!.cause!!.message)
        }
    }

    @Test
    fun `IOException should return NetworkException`() = runTest {
        given(freeApi)
            .coroutine { getConversion(base) }
            .thenThrow(IOException(exception.message.toString()))

        assertFailsWith(NetworkException::class) {
            subject.getConversion(base)
        }.let {
            assertNotNull(it.cause)
            assertEquals(exception.message, it.cause!!.message)
        }
    }

    @Test
    fun `ConnectTimeoutException should return TimeoutException`() = runTest {
        given(freeApi)
            .coroutine { getConversion(base) }
            .thenThrow(ConnectTimeoutException(exception.message.toString()))

        assertFailsWith(TimeoutException::class) {
            subject.getConversion(base)
        }.let {
            assertNotNull(it.cause)
            assertEquals(exception.message, it.cause!!.message)
        }
    }

    @Test
    fun `SerializationException should return ModelMappingException`() = runTest {
        given(freeApi)
            .coroutine { getConversion(base) }
            .thenThrow(SerializationException(exception))

        assertFailsWith(ModelMappingException::class) {
            subject.getConversion(base)
        }.let {
            assertNotNull(it.cause)
            assertNotNull(it.cause!!.cause)
            assertEquals(exception, it.cause!!.cause)
            assertEquals(exception.message, it.cause!!.cause!!.message)
        }
    }

    @Test
    fun `Any other exception should return UnknownNetworkException`() = runTest {
        given(freeApi)
            .coroutine { getConversion(base) }
            .thenThrow(Exception(exception))

        assertFailsWith(UnknownNetworkException::class) {
            subject.getConversion(base)
        }.let {
            assertNotNull(it.cause)
            assertNotNull(it.cause!!.cause)
            assertEquals(exception, it.cause!!.cause)
            assertEquals(exception.message, it.cause!!.cause!!.message)
        }
    }
}
