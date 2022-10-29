package com.oztechan.ccc.common.service

import com.oztechan.ccc.common.api.free.FreeApi
import com.oztechan.ccc.common.api.model.CurrencyResponse
import com.oztechan.ccc.common.api.model.Rates
import com.oztechan.ccc.common.error.UnknownNetworkException
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.service.free.FreeApiService
import com.oztechan.ccc.common.service.free.FreeApiServiceImpl
import com.oztechan.ccc.test.BaseSubjectTest
import com.oztechan.ccc.test.util.createTestDispatcher
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@Suppress("OPT_IN_USAGE")
internal class FreeApiServiceTest : BaseSubjectTest<FreeApiService>() {

    override val subject: FreeApiService by lazy {
        FreeApiServiceImpl(freeApi, createTestDispatcher())
    }

    @Mock
    private val freeApi = mock(classOf<FreeApi>())

    private val mockEntity = CurrencyResponse("EUR", "12.21.2121", Rates())
    private val mockThrowable = Throwable("mock")
    private val mockBase = "EUR"

    @Test
    fun getRates_parameter_can_not_be_empty() = runTest {
        runCatching { subject.getRates("") }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertIs<UnknownNetworkException>(it.exceptionOrNull())
        }

        verify(freeApi)
            .coroutine { freeApi.getRates("") }
            .wasInvoked()
    }

    @Test
    fun getRates_error() = runTest {
        given(freeApi)
            .coroutine { freeApi.getRates(mockBase) }
            .thenThrow(mockThrowable)

        runCatching { subject.getRates(mockBase) }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertNotNull(it.exceptionOrNull())
            assertNotNull(it.exceptionOrNull()!!.cause)
            assertNotNull(it.exceptionOrNull()!!.message)
            assertEquals(mockThrowable.message, it.exceptionOrNull()!!.cause!!.message)
            assertIs<UnknownNetworkException>(it.exceptionOrNull())
        }

        verify(freeApi)
            .coroutine { getRates(mockBase) }
            .wasInvoked()
    }

    @Test
    fun getRates_success() = runTest {
        given(freeApi)
            .coroutine { freeApi.getRates(mockBase) }
            .thenReturn(mockEntity)

        runCatching { subject.getRates(mockBase) }.let {
            assertTrue { it.isSuccess }
            assertFalse { it.isFailure }
            assertEquals(mockEntity.toModel(), it.getOrNull())
        }

        verify(freeApi)
            .coroutine { getRates(mockBase) }
            .wasInvoked()
    }
}
