/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.repo

import com.github.mustafaozhan.ccc.common.api.repo.ApiRepository
import com.github.mustafaozhan.ccc.common.api.repo.ApiRepositoryImpl
import com.github.mustafaozhan.ccc.common.api.service.ApiService
import com.github.mustafaozhan.ccc.common.entity.CurrencyResponseEntity
import com.github.mustafaozhan.ccc.common.entity.RatesEntity
import com.github.mustafaozhan.ccc.common.mapper.toModel
import com.github.mustafaozhan.ccc.common.model.EmptyParameterException
import com.github.mustafaozhan.ccc.common.runTest
import com.github.mustafaozhan.logmob.initLogger
import io.mockative.Mock
import io.mockative.any
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ApiRepositoryTest {

    @Mock
    private val apiService = mock(classOf<ApiService>())

    private val repository: ApiRepository by lazy {
        ApiRepositoryImpl(apiService)
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
            .suspendFunction(apiService::getRatesByAPI)
            .whenInvokedWith(any())
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
            .suspendFunction(apiService::getRatesByPremiumAPI)
            .whenInvokedWith(any())
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
            .suspendFunction(apiService::getRatesByBackend)
            .whenInvokedWith(any())
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
            .suspendFunction(apiService::getRatesByAPI)
            .whenInvokedWith(any())
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
            .suspendFunction(apiService::getRatesByPremiumAPI)
            .whenInvokedWith(any())
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
            .suspendFunction(apiService::getRatesByBackend)
            .whenInvokedWith(any())
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
