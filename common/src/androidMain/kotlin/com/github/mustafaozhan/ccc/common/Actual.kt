/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import com.github.mustafaozhan.ccc.common.db.sql.CurrencyConverterCalculatorDatabase
import com.github.mustafaozhan.ccc.common.di.DATABASE_NAME
import com.github.mustafaozhan.ccc.common.model.PlatformType
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.android.AndroidSqliteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.koin.core.module.Module
import kotlin.coroutines.CoroutineContext

actual val platform = PlatformType.ANDROID

actual val platformCoroutineContext: CoroutineContext = Dispatchers.IO

actual fun Module.getSettingsDefinition() = single<Settings> { AndroidSettings(get()) }

actual fun Module.getDatabaseDefinition() = single {
    CurrencyConverterCalculatorDatabase(
        AndroidSqliteDriver(
            CurrencyConverterCalculatorDatabase.Schema,
            get(),
            DATABASE_NAME
        )
    )
}

actual fun runTest(block: suspend () -> Unit) = runBlocking { block() }

actual fun isDebug() = BuildConfig.DEBUG
