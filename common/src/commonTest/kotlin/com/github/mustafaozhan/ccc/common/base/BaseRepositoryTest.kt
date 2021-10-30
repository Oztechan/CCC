/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.base

import co.touchlab.kermit.Logger
import com.github.mustafaozhan.ccc.common.di.modules.apiModule
import com.github.mustafaozhan.ccc.common.di.modules.getDatabaseModule
import com.github.mustafaozhan.ccc.common.di.modules.getSettingsModule
import com.github.mustafaozhan.logmob.initLogger
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

abstract class BaseRepositoryTest<SubjectType> {
    protected lateinit var koin: Koin
    protected abstract val repository: SubjectType

    @BeforeTest
    fun setup() {
        initLogger(true).let {
            it.v { "Logger initialized" }
        }

        Logger.v { "BaseRepositoryTest setup" }

        startKoin {
            modules(
                apiModule,
                getDatabaseModule(true),
                getSettingsModule(true)
            )
        }.also {
            koin = it.koin
        }
    }

    @AfterTest
    fun destroy() {
        Logger.v { "BaseRepositoryTest destroy" }
        stopKoin()
    }
}
