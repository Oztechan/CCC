package com.oztechan.ccc.common.service

import com.oztechan.ccc.common.api.backend.BackendApi
import com.oztechan.ccc.common.api.model.Conversion
import com.oztechan.ccc.common.api.model.CurrencyResponse
import com.oztechan.ccc.common.error.UnknownNetworkException
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.service.backend.BackendApiService
import com.oztechan.ccc.common.service.backend.BackendApiServiceImpl
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
internal class BackendApiServiceTest : BaseSubjectTest<BackendApiService>() {

    override val subject: BackendApiService by lazy {
        BackendApiServiceImpl(backendApi, createTestDispatcher())
    }

    @Mock
    private val backendApi = mock(classOf<BackendApi>())

    private val mockEntity = CurrencyResponse("EUR", "12.21.2121", Conversion())
    private val mockThrowable = Throwable("mock")
    private val mockBase = "EUR"

    @Test
    fun getConversion_parameter_can_not_be_empty() = runTest {
        runCatching { subject.getConversion("") }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertIs<UnknownNetworkException>(it.exceptionOrNull())
        }

        verify(backendApi)
            .coroutine { backendApi.getConversion("") }
            .wasInvoked()
    }

    @Test
    fun getConversion_error() = runTest {
        given(backendApi)
            .coroutine { backendApi.getConversion(mockBase) }
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

        verify(backendApi)
            .coroutine { getConversion(mockBase) }
            .wasInvoked()
    }

    @Test
    fun getConversion_success() = runTest {
        given(backendApi)
            .coroutine { backendApi.getConversion(mockBase) }
            .thenReturn(mockEntity)

        runCatching { subject.getConversion(mockBase) }.let {
            assertTrue { it.isSuccess }
            assertFalse { it.isFailure }
            assertEquals(mockEntity.toModel(), it.getOrNull())
        }

        verify(backendApi)
            .coroutine { getConversion(mockBase) }
            .wasInvoked()
    }
}
