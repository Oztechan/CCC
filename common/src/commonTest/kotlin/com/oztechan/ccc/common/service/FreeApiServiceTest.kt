package com.oztechan.ccc.common.service

import com.oztechan.ccc.common.api.free.FreeApi
import com.oztechan.ccc.common.api.model.Conversion
import com.oztechan.ccc.common.api.model.ExchangeRate
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

    private val mockEntity = ExchangeRate("EUR", "12.21.2121", Conversion())
    private val mockThrowable = Throwable("mock")
    private val mockBase = "EUR"

    @Test
    fun `getConversion parameter can not be empty`() = runTest {
        runCatching { subject.getConversion("") }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertIs<UnknownNetworkException>(it.exceptionOrNull())
        }

        verify(freeApi)
            .coroutine { freeApi.getConversion("") }
            .wasInvoked()
    }

    @Test
    fun `getConversion error`() = runTest {
        given(freeApi)
            .coroutine { freeApi.getConversion(mockBase) }
            .thenThrow(mockThrowable)

        runCatching { subject.getConversion(mockBase) }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertNotNull(it.exceptionOrNull())
            assertNotNull(it.exceptionOrNull()!!.cause)
            assertNotNull(it.exceptionOrNull()!!.message)
            assertEquals(mockThrowable.message, it.exceptionOrNull()!!.cause!!.message)
            assertIs<UnknownNetworkException>(it.exceptionOrNull())
        }

        verify(freeApi)
            .coroutine { getConversion(mockBase) }
            .wasInvoked()
    }

    @Test
    fun `getConversion success`() = runTest {
        given(freeApi)
            .coroutine { freeApi.getConversion(mockBase) }
            .thenReturn(mockEntity)

        runCatching { subject.getConversion(mockBase) }.let {
            assertTrue { it.isSuccess }
            assertFalse { it.isFailure }
            assertEquals(mockEntity.toModel(), it.getOrNull())
        }

        verify(freeApi)
            .coroutine { getConversion(mockBase) }
            .wasInvoked()
    }
}
