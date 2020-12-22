/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.repo

import com.github.mustafaozhan.ccc.common.api.ApiRepository
import com.github.mustafaozhan.ccc.common.base.BaseRepositoryTest
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.error.EmptyParameterException
import com.github.mustafaozhan.ccc.common.error.NullBaseException
import com.github.mustafaozhan.ccc.common.model.CurrencyType
import com.github.mustafaozhan.ccc.common.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class ApiRepositoryTest : BaseRepositoryTest<ApiRepository>() {

    override val repository: ApiRepository by lazy {
        koin.getDependency(ApiRepository::class)
    }

    // defaults
    @Test
    fun emptyParameterException() = runTest {
        repository.getRatesByBaseViaApi("").execute({}, {
            assertTrue(it is EmptyParameterException)
        })
        repository.getRatesByBaseViaBackend("").execute({}, {
            assertTrue(it is EmptyParameterException)
        })
    }

    @Test
    fun nullBaseException() = runTest {
        repository.getRatesByBaseViaApi(CurrencyType.NULL.toString()).execute({}, {
            assertTrue(it is NullBaseException)
        })
        repository.getRatesByBaseViaBackend(CurrencyType.NULL.toString()).execute({}, {
            assertTrue(it is NullBaseException)
        })
    }
}
