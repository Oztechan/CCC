package com.oztechan.ccc.common.service

import com.oztechan.ccc.common.api.free.FreeApi
import com.oztechan.ccc.common.error.ModelMappingException
import com.oztechan.ccc.common.error.NetworkException
import com.oztechan.ccc.common.error.TimeoutException
import com.oztechan.ccc.common.error.UnknownNetworkException
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
import kotlin.test.assertFailsWith

@Suppress("OPT_IN_USAGE")
class BaseNetworkServiceTest : BaseSubjectTest<FreeApiService>() {
    override val subject: FreeApiService by lazy {
        FreeApiServiceImpl(freeApi, createTestDispatcher())
    }

    @Mock
    private val freeApi = mock(classOf<FreeApi>())

    private val base = "EUR"

    @Test
    fun `CancellationException should return exception itself`() = runTest {
        given(freeApi)
            .coroutine { getRates(base) }
            .thenThrow(CancellationException())

        assertFailsWith(CancellationException::class) {
            subject.getRates(base)
        }
    }

    @Test
    fun `IOException should return NetworkException`() = runTest {
        given(freeApi)
            .coroutine { getRates(base) }
            .thenThrow(IOException(""))

        assertFailsWith(NetworkException::class) {
            subject.getRates(base)
        }
    }

    @Test
    fun `ConnectTimeoutException should return TimeoutException`() = runTest {
        given(freeApi)
            .coroutine { getRates(base) }
            .thenThrow(ConnectTimeoutException(""))

        assertFailsWith(TimeoutException::class) {
            subject.getRates(base)
        }
    }

    @Test
    fun `SerializationException should return ModelMappingException`() = runTest {
        given(freeApi)
            .coroutine { getRates(base) }
            .thenThrow(SerializationException())

        assertFailsWith(ModelMappingException::class) {
            subject.getRates(base)
        }
    }

    @Test
    fun `Any other exception should return UnknownNetworkException`() = runTest {
        given(freeApi)
            .coroutine { getRates(base) }
            .thenThrow(Exception())

        assertFailsWith(UnknownNetworkException::class) {
            subject.getRates(base)
        }
    }
}
