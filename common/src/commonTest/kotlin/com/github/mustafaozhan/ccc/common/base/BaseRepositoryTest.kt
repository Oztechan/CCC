/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.base

import com.github.mustafaozhan.ccc.common.di.initCommon
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import org.koin.core.Koin
import org.koin.core.context.KoinContextHandler

abstract class BaseRepositoryTest<SubjectType> {
    protected lateinit var koin: Koin
    protected abstract val repository: SubjectType

    @BeforeTest
    fun setup() {
        initCommon(useFakes = true).also {
            koin = it.koin
        }
    }

    @AfterTest
    fun destroy() {
        KoinContextHandler.stop()
    }
}
