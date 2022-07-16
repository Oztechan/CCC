/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.common.repo

import com.github.submob.logmob.initLogger
import com.oztechan.ccc.common.api.repo.ApiRepository
import com.oztechan.ccc.common.api.repo.ApiRepositoryImpl
import com.oztechan.ccc.common.api.service.ApiService
import com.oztechan.ccc.common.entity.CurrencyResponseEntity
import com.oztechan.ccc.common.entity.RatesEntity
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.model.EmptyParameterException
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("OPT_IN_USAGE")
class ApiRepositoryTest {

    @Mock
    private val apiService = mock(classOf<ApiService>())

    private val repository: ApiRepository by lazy {
        ApiRepositoryImpl(apiService, newSingleThreadContext(this::class.simpleName.toString()))
    }

    private val mockEntity = CurrencyResponseEntity("EUR", "12.21.2121", RatesEntity())
    private val mockThrowable = Throwable("mock")
    private val mockBase = "EUR"

    @BeforeTest
    fun setup() {
        initLogger(true)
    }

    @Test
    fun getRatesByAPI_parameter_can_not_be_empty() = runTest {
        runCatching { repository.getRatesByAPI("") }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertTrue { it.exceptionOrNull() is EmptyParameterException }
        }

        verify(apiService)
            .coroutine { apiService.getRatesByAPI("") }
            .wasInvoked()
    }

    @Test
    fun getRatesByPremiumAPI_parameter_can_not_be_empty() = runTest {
        runCatching { repository.getRatesByPremiumAPI("") }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertTrue { it.exceptionOrNull() is EmptyParameterException }
        }

        verify(apiService)
            .coroutine { apiService.getRatesByPremiumAPI("") }
            .wasInvoked()
    }

    @Test
    fun getRatesByBackend_parameter_can_not_be_empty() = runTest {
        runCatching { repository.getRatesByBackend("") }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertTrue { it.exceptionOrNull() is EmptyParameterException }
        }

        verify(apiService)
            .coroutine { apiService.getRatesByBackend("") }
            .wasInvoked()
    }

    @Test
    fun getRatesByAPI_error() = runTest {
        given(apiService)
            .coroutine { apiService.getRatesByAPI(mockBase) }
            .thenThrow(mockThrowable)

        runCatching { repository.getRatesByAPI(mockBase) }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertEquals(mockThrowable.message, it.exceptionOrNull()?.message)
            assertEquals(mockThrowable.toString(), it.exceptionOrNull().toString())
        }

        verify(apiService)
            .coroutine { getRatesByAPI(mockBase) }
            .wasInvoked()
    }

    @Test
    fun getRatesByPremiumAPI_error() = runTest {
        given(apiService)
            .coroutine { apiService.getRatesByPremiumAPI(mockBase) }
            .thenThrow(mockThrowable)

        runCatching { repository.getRatesByPremiumAPI(mockBase) }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertEquals(mockThrowable.message, it.exceptionOrNull()?.message)
            assertEquals(mockThrowable.toString(), it.exceptionOrNull().toString())
        }

        verify(apiService)
            .coroutine { getRatesByPremiumAPI(mockBase) }
            .wasInvoked()
    }

    @Test
    fun getRatesByBackend_error() = runTest {
        given(apiService)
            .coroutine { apiService.getRatesByBackend(mockBase) }
            .thenThrow(mockThrowable)

        runCatching { repository.getRatesByBackend(mockBase) }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertEquals(mockThrowable.message, it.exceptionOrNull()?.message)
            assertEquals(mockThrowable.toString(), it.exceptionOrNull().toString())
        }

        verify(apiService)
            .coroutine { getRatesByBackend(mockBase) }
            .wasInvoked()
    }

    @Test
    fun getRatesByAPI_success() = runTest {
        given(apiService)
            .coroutine { apiService.getRatesByAPI(mockBase) }
            .thenReturn(mockEntity)

        runCatching { repository.getRatesByAPI(mockBase) }.let {
            assertTrue { it.isSuccess }
            assertFalse { it.isFailure }
            assertEquals(mockEntity.toModel(), it.getOrNull())
        }

        verify(apiService)
            .coroutine { getRatesByAPI(mockBase) }
            .wasInvoked()
    }

    @Test
    fun getRatesByPremiumAPI_success() = runTest {
        given(apiService)
            .coroutine { apiService.getRatesByPremiumAPI(mockBase) }
            .thenReturn(mockEntity)

        runCatching { repository.getRatesByPremiumAPI(mockBase) }.let {
            assertTrue { it.isSuccess }
            assertFalse { it.isFailure }
            assertEquals(mockEntity.toModel(), it.getOrNull())
        }

        verify(apiService)
            .coroutine { getRatesByPremiumAPI(mockBase) }
            .wasInvoked()
    }

    @Test
    fun getRatesByBackend_success() = runTest {
        given(apiService)
            .coroutine { apiService.getRatesByBackend(mockBase) }
            .thenReturn(mockEntity)

        runCatching { repository.getRatesByBackend(mockBase) }.let {
            assertTrue { it.isSuccess }
            assertFalse { it.isFailure }
            assertEquals(mockEntity.toModel(), it.getOrNull())
        }

        verify(apiService)
            .coroutine { getRatesByBackend(mockBase) }
            .wasInvoked()
    }
}
