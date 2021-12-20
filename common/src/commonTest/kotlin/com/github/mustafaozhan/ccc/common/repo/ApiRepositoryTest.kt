/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.repo

import com.github.mustafaozhan.ccc.common.api.repo.ApiRepository
import com.github.mustafaozhan.ccc.common.api.repo.ApiRepositoryImpl
import com.github.mustafaozhan.ccc.common.api.service.ApiService
import com.github.mustafaozhan.ccc.common.model.EmptyParameterException
import com.github.mustafaozhan.ccc.common.runTest
import com.github.mustafaozhan.logmob.initLogger
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.mock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class ApiRepositoryTest {

    @Mock
    private val apiService = mock(classOf<ApiService>())

    private val repository: ApiRepository by lazy {
        ApiRepositoryImpl(apiService)
    }

    @BeforeTest
    fun setup() {
        initLogger(true)
    }

    @Test
    fun getRatesByAPIParameterCanNotBeEmpty() = runTest {
        repository.getRatesByAPI("").execute(error = {
            assertTrue { it is EmptyParameterException }
        })
    }

    @Test
    fun getRatesByPremiumAPIParameterCanNotBeEmpty() = runTest {
        repository.getRatesByPremiumAPI("")
            .execute(error = {
                assertTrue { it is EmptyParameterException }
            })
    }

    @Test
    fun getRatesByBackendParameterCanNotBeEmpty() = runTest {
        repository.getRatesByBackend("").execute(error = {
            assertTrue { it is EmptyParameterException }
        })
    }
}
