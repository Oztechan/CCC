/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import com.github.mustafaozhan.ccc.common.model.PlatformType
import com.github.mustafaozhan.ccc.common.sql.CurrencyConverterCalculatorDatabase
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.JvmPreferencesSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.koin.core.module.Module
import kotlin.coroutines.CoroutineContext

actual val platform = PlatformType.JVM

actual val platformCoroutineContext: CoroutineContext = Dispatchers.IO

@ExperimentalSettingsImplementation
actual fun Module.getSettingsDefinition() = single<Settings> { JvmPreferencesSettings(get()) }

actual fun Module.getDatabaseDefinition() = single {
    CurrencyConverterCalculatorDatabase(
        JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
            .also { CurrencyConverterCalculatorDatabase.Schema.create(it) }
    )
}

actual fun runTest(block: suspend () -> Unit) = runBlocking { block() }

// todo need to find correct implementation for JS
@Suppress("FunctionOnlyReturningConstant")
actual fun isDebug() = false
