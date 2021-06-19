package com.github.mustafaozhan.ccc.common.di

import com.github.mustafaozhan.ccc.common.db.sql.CurrencyConverterCalculatorDatabase
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.android.AndroidSqliteDriver
import org.koin.core.module.Module

actual fun Module.getSettingsDefinition() = single<Settings> {
    AndroidSettings(get())
}

actual fun Module.getDatabaseDefinition(databaseName: String) = single {
    CurrencyConverterCalculatorDatabase(
        AndroidSqliteDriver(
            CurrencyConverterCalculatorDatabase.Schema,
            get(),
            databaseName
        )
    )
}
