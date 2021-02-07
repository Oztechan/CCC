/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.base

import com.github.mustafaozhan.ccc.client.di.initClient
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import org.koin.core.Koin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

abstract class BaseViewModelTest<ViewModelType> {
    protected lateinit var koin: Koin
    protected abstract val viewModel: ViewModelType

    @BeforeTest
    fun setup() {
        initClient(module { }, true).also {
            koin = it.koin
        }
    }

    @AfterTest
    fun destroy() = stopKoin()
}
