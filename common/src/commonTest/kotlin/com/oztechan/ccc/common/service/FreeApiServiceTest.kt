package com.oztechan.ccc.common.service

import com.oztechan.ccc.common.api.free.FreeApi
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.model.EmptyParameterException
import com.oztechan.ccc.common.service.free.FreeApiService
import com.oztechan.ccc.common.service.free.FreeApiServiceImpl
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
class FreeApiServiceTest : BaseServiceTest<FreeApiService>() {
    @Mock
    private val freeApi = mock(classOf<FreeApi>())

    override var service: FreeApiService = FreeApiServiceImpl(
        freeApi,
        newSingleThreadContext(this::class.simpleName.toString())
    )

    @Test
    fun getRates_parameter_can_not_be_empty() = runTest {
        runCatching { service.getRates("") }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertTrue { it.exceptionOrNull() is EmptyParameterException }
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

        runCatching { service.getRates(mockBase) }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertEquals(mockThrowable.message, it.exceptionOrNull()?.message)
            assertEquals(mockThrowable.toString(), it.exceptionOrNull().toString())
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

        runCatching { service.getRates(mockBase) }.let {
            assertTrue { it.isSuccess }
            assertFalse { it.isFailure }
            assertEquals(mockEntity.toModel(), it.getOrNull())
        }

        verify(freeApi)
            .coroutine { getRates(mockBase) }
            .wasInvoked()
    }
}
