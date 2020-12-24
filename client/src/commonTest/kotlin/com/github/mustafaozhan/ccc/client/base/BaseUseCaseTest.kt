/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.base

import com.github.mustafaozhan.ccc.client.di.initClient
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import org.koin.core.Koin
import org.koin.core.context.KoinContextHandler
import org.koin.dsl.module

abstract class BaseUseCaseTest<UseCaseType> {
    protected lateinit var koin: Koin
    protected abstract val useCase: UseCaseType

    @BeforeTest
    fun setup() {
        initClient(module { }, true).also {
            koin = it.koin
        }
    }

    @AfterTest
    fun destroy() {
        KoinContextHandler.stop()
    }
}
