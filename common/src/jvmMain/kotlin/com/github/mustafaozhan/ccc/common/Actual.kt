/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import com.github.mustafaozhan.ccc.common.model.CommonPlatformType
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

actual val commonPlatformType = CommonPlatformType.JVM

actual val platformCoroutineContext: CoroutineContext = Dispatchers.IO

actual val platformCommonModule: Module = module {
    single {
        CurrencyConverterCalculatorDatabase(
            JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
                .also { CurrencyConverterCalculatorDatabase.Schema.create(it) }
        )
    }
}
