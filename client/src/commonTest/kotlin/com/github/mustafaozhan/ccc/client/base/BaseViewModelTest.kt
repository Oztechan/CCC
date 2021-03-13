/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.base

import com.github.mustafaozhan.ccc.client.di.initClient
import com.github.mustafaozhan.logmob.kermit
import org.koin.core.Koin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

abstract class BaseViewModelTest<ViewModelType> {
    protected lateinit var koin: Koin
    protected abstract val viewModel: ViewModelType

    @BeforeTest
    fun setup() {
        initClient(module { }, true).also {
            koin = it.koin
        }
        kermit.d { "BaseViewModelTest setup" }
    }

    @AfterTest
    fun destroy() {
        kermit.d { "BaseViewModelTest destroy" }
        stopKoin()
    }
}
