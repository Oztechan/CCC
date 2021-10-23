package com.github.mustafaozhan.ccc.common.di

import com.github.mustafaozhan.ccc.common.db.sql.CurrencyConverterCalculatorDatabase
import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import org.koin.core.module.Module

actual fun Module.getSettingsDefinition() = single<Settings> {
    AppleSettings(get<NativeDependencyWrapper>().userDefaults)
}

actual fun Module.getDatabaseDefinition(databaseName: String) = single {
    CurrencyConverterCalculatorDatabase(
        NativeSqliteDriver(
            CurrencyConverterCalculatorDatabase.Schema,
            databaseName
        )
    )
}
