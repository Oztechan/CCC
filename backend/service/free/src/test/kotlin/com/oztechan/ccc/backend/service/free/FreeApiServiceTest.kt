package com.oztechan.ccc.backend.service.free

import com.oztechan.ccc.common.core.network.api.free.FreeApi
import com.oztechan.ccc.common.core.network.mapper.toConversionModel
import com.oztechan.ccc.common.core.network.model.Conversion
import com.oztechan.ccc.common.core.network.model.ExchangeRate
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@Suppress("OPT_IN_USAGE")
internal class FreeApiServiceTest {

    private val subject: FreeApiService by lazy {
        FreeApiServiceImpl(freeApi, newSingleThreadContext(this::class.simpleName.toString()))
    }

    @Mock
    private val freeApi = mock(classOf<FreeApi>())

    private val exchangeRate = ExchangeRate("EUR", "12.21.2121", Conversion())
    private val throwable = Throwable("mock")
    private val base = "EUR"

    @Test
    fun `getConversion parameter can not be empty`() = runTest {
        runCatching { subject.getConversion("") }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
        }

        verify(freeApi)
            .coroutine { freeApi.getConversion("") }
            .wasInvoked()
    }

    @Test
    fun `getConversion error`() = runTest {
        given(freeApi)
            .coroutine { freeApi.getConversion(base) }
            .thenThrow(throwable)

        runCatching { subject.getConversion(base) }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertNotNull(it.exceptionOrNull())
            assertNotNull(it.exceptionOrNull()!!.cause)
            assertNotNull(it.exceptionOrNull()!!.message)
            assertEquals(throwable.message, it.exceptionOrNull()!!.cause!!.message)
        }

        verify(freeApi)
            .coroutine { getConversion(base) }
            .wasInvoked()
    }

    @Test
    fun `getConversion success`() = runTest {
        given(freeApi)
            .coroutine { freeApi.getConversion(base) }
            .thenReturn(exchangeRate)

        runCatching { subject.getConversion(base) }.let {
            assertTrue { it.isSuccess }
            assertFalse { it.isFailure }
            assertEquals(exchangeRate.conversion.toConversionModel(), it.getOrNull())
        }

        verify(freeApi)
            .coroutine { getConversion(base) }
            .wasInvoked()
    }
}
