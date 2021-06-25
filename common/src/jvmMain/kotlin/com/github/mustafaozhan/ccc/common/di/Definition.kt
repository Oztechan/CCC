package com.github.mustafaozhan.ccc.common.di

import com.github.mustafaozhan.ccc.common.db.sql.CurrencyConverterCalculatorDatabase
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.JvmPreferencesSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.koin.core.module.Module

@ExperimentalSettingsImplementation
actual fun Module.getSettingsDefinition() = single<Settings> {
    JvmPreferencesSettings(get())
}

actual fun Module.getDatabaseDefinition(databaseName: String) = single {
    CurrencyConverterCalculatorDatabase(
        JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
            .also { CurrencyConverterCalculatorDatabase.Schema.create(it) }
    )
}
