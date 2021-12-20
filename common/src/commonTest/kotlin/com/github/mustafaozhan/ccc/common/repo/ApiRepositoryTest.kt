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
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
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
        repository.getRatesByAPI("").execute(
            error = {
                assertTrue { it is EmptyParameterException }
            }
        )
    }

    @Test
    fun getRatesByPremiumAPI_parameter_can_not_be_empty() = runTest {
        repository.getRatesByPremiumAPI("").execute(
            error = {
                assertTrue { it is EmptyParameterException }
            }
        )
    }

    @Test
    fun getRatesByBackend_parameter_can_not_be_empty() = runTest {
        repository.getRatesByBackend("").execute(
            error = {
                assertTrue { it is EmptyParameterException }
            }
        )
    }

    @Test
    fun getRatesByAPI_error() = runTest {
        given(apiService)
            .suspendFunction(apiService::getRatesByAPI)
            .whenInvokedWith(any())
            .thenThrow(mockThrowable)

        repository.getRatesByAPI(mockBase).execute(
            success = { assertTrue { false } },
            error = {
                assertEquals(mockThrowable.message, it.message)
                assertEquals(mockThrowable.toString(), it.toString())
            }
        )
    }

    @Test
    fun getRatesByPremiumAPI_error() = runTest {
        given(apiService)
            .suspendFunction(apiService::getRatesByPremiumAPI)
            .whenInvokedWith(any())
            .thenThrow(mockThrowable)

        repository.getRatesByPremiumAPI(mockBase).execute(
            success = { assertTrue { false } },
            error = {
                assertEquals(mockThrowable.message, it.message)
                assertEquals(mockThrowable.toString(), it.toString())
            }
        )
    }

    @Test
    fun getRatesByBackend_error() = runTest {
        given(apiService)
            .suspendFunction(apiService::getRatesByBackend)
            .whenInvokedWith(any())
            .thenThrow(mockThrowable)

        repository.getRatesByBackend(mockBase).execute(
            success = { assertTrue { false } },
            error = {
                assertEquals(mockThrowable.message, it.message)
                assertEquals(mockThrowable.toString(), it.toString())
            }
        )
    }

    @Test
    fun getRatesByAPI_success() = runTest {
        given(apiService)
            .suspendFunction(apiService::getRatesByAPI)
            .whenInvokedWith(any())
            .thenReturn(mockEntity)

        repository.getRatesByAPI(mockBase).execute(
            success = { assertTrue { it == mockEntity.toModel() } },
            error = { assertTrue { false } }
        )
    }

    @Test
    fun getRatesByPremiumAPI_success() = runTest {
        given(apiService)
            .suspendFunction(apiService::getRatesByPremiumAPI)
            .whenInvokedWith(any())
            .thenReturn(mockEntity)

        repository.getRatesByPremiumAPI(mockBase).execute(
            success = { assertTrue { it == mockEntity.toModel() } },
            error = { assertTrue { false } }
        )
    }

    @Test
    fun getRatesByBackend_success() = runTest {
        given(apiService)
            .suspendFunction(apiService::getRatesByBackend)
            .whenInvokedWith(any())
            .thenReturn(mockEntity)

        repository.getRatesByBackend(mockBase).execute(
            success = { assertTrue { it == mockEntity.toModel() } },
            error = { assertTrue { false } }
        )
    }
}
