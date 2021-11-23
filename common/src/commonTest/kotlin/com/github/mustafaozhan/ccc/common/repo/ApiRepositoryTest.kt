/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.repo

import com.github.mustafaozhan.ccc.common.api.ApiRepository
import com.github.mustafaozhan.ccc.common.base.BaseRepositoryTest
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.error.EmptyParameterException
import com.github.mustafaozhan.ccc.common.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class ApiRepositoryTest : BaseRepositoryTest<ApiRepository>() {

    override val repository: ApiRepository by lazy {
        koin.getDependency(ApiRepository::class)
    }

    @Test
    fun getUnPopularRatesParameterCanNotBeEmpty() = runTest {
        repository.getUnPopularRates("").execute(error = {
            assertTrue(it is EmptyParameterException)
        })
    }

    @Test
    fun getPopularRatesParameterCanNotBeEmpty() = runTest {
        repository.getPopularRates("").execute(error = {
            assertTrue(it is EmptyParameterException)
        })
    }

    @Test
    fun getRatesViaBackendParameterCanNotBeEmpty() = runTest {
        repository.getRatesViaBackend("").execute(error = {
            assertTrue(it is EmptyParameterException)
        })
    }
}
