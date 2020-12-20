/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import com.github.mustafaozhan.ccc.common.model.PlatformType
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platform = PlatformType.JVM

actual val platformCoroutineContext: CoroutineContext = Dispatchers.IO

actual val platformCommonModule: Module = module {
    single {
        CurrencyConverterCalculatorDatabase(
            JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
                .also { CurrencyConverterCalculatorDatabase.Schema.create(it) }
        )
    }
}

actual fun runTest(block: suspend (scope: CoroutineScope) -> Unit) = runBlocking { block(this) }
