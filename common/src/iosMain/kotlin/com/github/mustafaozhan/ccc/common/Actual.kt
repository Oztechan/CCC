/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import com.github.mustafaozhan.ccc.common.di.DATABASE_NAME
import com.github.mustafaozhan.ccc.common.fake.FakeSettings
import com.github.mustafaozhan.ccc.common.model.PlatformType
import com.github.mustafaozhan.ccc.common.sql.CurrencyConverterCalculatorDatabase
import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

actual val platform = PlatformType.IOS

actual val platformCoroutineContext: CoroutineContext = Dispatchers.Default

lateinit var nsUserDefaults: NSUserDefaults
actual fun getPlatformCommonModule(useFakes: Boolean): Module = module {
    if (useFakes) {
        single { FakeSettings.getSettings() }
    } else {
        single<Settings> { AppleSettings(nsUserDefaults) }
    }
    single {
        CurrencyConverterCalculatorDatabase(
            NativeSqliteDriver(
                CurrencyConverterCalculatorDatabase.Schema,
                DATABASE_NAME
            )
        )
    }
}

actual fun runTest(block: suspend () -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        block()
    }
}
