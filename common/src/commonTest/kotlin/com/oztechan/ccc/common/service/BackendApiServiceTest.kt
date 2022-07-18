package com.oztechan.ccc.common.service

import com.oztechan.ccc.common.api.backend.BackendApi
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.model.EmptyParameterException
import com.oztechan.ccc.common.service.backend.BackendApiService
import com.oztechan.ccc.common.service.backend.BackendApiServiceImpl
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
import kotlin.test.assertTrue

@Suppress("OPT_IN_USAGE")
class BackendApiServiceTest : BaseServiceTest<BackendApiService>() {
    @Mock
    private val backendApi = mock(classOf<BackendApi>())

    override var service: BackendApiService = BackendApiServiceImpl(
        backendApi,
        newSingleThreadContext(this::class.simpleName.toString())
    )

    @Test
    fun getRates_parameter_can_not_be_empty() = runTest {
        runCatching { service.getRates("") }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertTrue { it.exceptionOrNull() is EmptyParameterException }
        }

        verify(backendApi)
            .coroutine { backendApi.getRates("") }
            .wasInvoked()
    }

    @Test
    fun getRates_error() = runTest {
        given(backendApi)
            .coroutine { backendApi.getRates(mockBase) }
            .thenThrow(mockThrowable)

        runCatching { service.getRates(mockBase) }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertEquals(mockThrowable.message, it.exceptionOrNull()?.message)
            assertEquals(mockThrowable.toString(), it.exceptionOrNull().toString())
        }

        verify(backendApi)
            .coroutine { getRates(mockBase) }
            .wasInvoked()
    }

    @Test
    fun getRates_success() = runTest {
        given(backendApi)
            .coroutine { backendApi.getRates(mockBase) }
            .thenReturn(mockEntity)

        runCatching { service.getRates(mockBase) }.let {
            assertTrue { it.isSuccess }
            assertFalse { it.isFailure }
            assertEquals(mockEntity.toModel(), it.getOrNull())
        }

        verify(backendApi)
            .coroutine { getRates(mockBase) }
            .wasInvoked()
    }
}
