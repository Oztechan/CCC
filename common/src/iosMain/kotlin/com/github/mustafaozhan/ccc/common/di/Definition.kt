package com.github.mustafaozhan.ccc.common.di

import com.github.mustafaozhan.ccc.common.db.sql.CurrencyConverterCalculatorDatabase
import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import org.koin.core.module.Module
import platform.Foundation.NSUserDefaults

lateinit var nsUserDefaults: NSUserDefaults

actual fun Module.getSettingsDefinition() = single<Settings> {
    AppleSettings(nsUserDefaults)
}

actual fun Module.getDatabaseDefinition(databaseName: String) = single {
    CurrencyConverterCalculatorDatabase(
        NativeSqliteDriver(
            CurrencyConverterCalculatorDatabase.Schema,
            databaseName
        )
    )
}
