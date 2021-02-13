/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import com.github.mustafaozhan.ccc.common.fake.FakeSettings
import com.github.mustafaozhan.ccc.common.model.PlatformType
import com.github.mustafaozhan.ccc.common.sql.CurrencyConverterCalculatorDatabase
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.JvmPreferencesSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platform = PlatformType.JVM

actual val platformCoroutineContext: CoroutineContext = Dispatchers.IO

@ExperimentalSettingsImplementation
actual fun getPlatformCommonModule(useFakes: Boolean): Module = module {
    if (useFakes) {
        single { FakeSettings.getSettings() }
    } else {
        single<Settings> { JvmPreferencesSettings(get()) }
    }
    single {
        CurrencyConverterCalculatorDatabase(
            JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
                .also { CurrencyConverterCalculatorDatabase.Schema.create(it) }
        )
    }
}

actual fun runTest(block: suspend () -> Unit) = runBlocking { block() }
