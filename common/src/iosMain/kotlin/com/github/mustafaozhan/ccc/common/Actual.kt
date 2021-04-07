/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import com.github.mustafaozhan.ccc.common.db.sql.CurrencyConverterCalculatorDatabase
import com.github.mustafaozhan.ccc.common.di.DATABASE_NAME
import com.github.mustafaozhan.ccc.common.model.PlatformType
import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.module.Module
import platform.Foundation.NSUserDefaults
import kotlin.coroutines.CoroutineContext

actual val platform = PlatformType.IOS

actual val platformCoroutineContext: CoroutineContext = Dispatchers.Default

lateinit var nsUserDefaults: NSUserDefaults

actual fun Module.getSettingsDefinition() = single<Settings> { AppleSettings(nsUserDefaults) }

actual fun Module.getDatabaseDefinition() = single {
    CurrencyConverterCalculatorDatabase(
        NativeSqliteDriver(
            CurrencyConverterCalculatorDatabase.Schema,
            DATABASE_NAME
        )
    )
}

actual fun runTest(block: suspend () -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        block()
    }
}

actual fun isDebug() = Platform.isDebugBinary
