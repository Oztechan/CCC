package com.oztechan.ccc.backend.service.free

import com.oztechan.ccc.common.core.network.api.free.FreeApi
import com.oztechan.ccc.common.core.network.mapper.toConversionModel
import com.oztechan.ccc.common.core.network.model.Conversion
import com.oztechan.ccc.common.core.network.model.ExchangeRate
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.coEvery
import io.mockative.coVerify
import io.mockative.mock
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class FreeApiServiceTest {

    private val subject: FreeApiService by lazy {
        @Suppress("OPT_IN_USAGE")
        FreeApiServiceImpl(freeApi, UnconfinedTestDispatcher())
    }

    @Mock
    private val freeApi: FreeApi = mock(classOf())

    private val exchangeRate = ExchangeRate("EUR", "12.21.2121", Conversion())
    private val throwable = Throwable("mock")
    private val base = "EUR"

    @Test
    fun `getConversion parameter can not be empty API call is not made`() = runTest {
        runCatching { subject.getConversion("") }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
        }

        coVerify { freeApi.getExchangeRate("") }
            .wasNotInvoked()
    }

    @Test
    fun `getConversion error`() = runTest {
        coEvery { freeApi.getExchangeRate(base) }
            .throws(throwable)

        runCatching { subject.getConversion(base) }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertNotNull(it.exceptionOrNull())
            assertNotNull(it.exceptionOrNull()!!.cause)
            assertNotNull(it.exceptionOrNull()!!.message)
            assertEquals(throwable.message, it.exceptionOrNull()!!.cause!!.message)
        }

        coVerify { freeApi.getExchangeRate(base) }
            .wasInvoked()
    }

    @Test
    fun `getConversion success`() = runTest {
        coEvery { freeApi.getExchangeRate(base) }
            .returns(exchangeRate)

        runCatching { subject.getConversion(base) }.let {
            assertTrue { it.isSuccess }
            assertFalse { it.isFailure }
            assertNotNull(it.getOrNull())
            assertEquals(exchangeRate.toConversionModel(), it.getOrNull())
        }

        coVerify { freeApi.getExchangeRate(base) }
            .wasInvoked()
    }
}
