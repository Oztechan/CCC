/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import com.github.mustafaozhan.ccc.common.CurrencyConverterCalculatorDatabase
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformName by lazy { "JVM" }
actual val platformVersion = KotlinVersion.CURRENT.toString()

actual val platformCoroutineContext: CoroutineContext = Dispatchers.IO

actual val platformCommonModule: Module = module {
    single {
        CurrencyConverterCalculatorDatabase(
            JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
                .also { CurrencyConverterCalculatorDatabase.Schema.create(it) }
        )
    }
}
