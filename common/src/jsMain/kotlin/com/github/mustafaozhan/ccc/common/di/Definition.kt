package com.github.mustafaozhan.ccc.common.di

import com.github.mustafaozhan.ccc.common.db.sql.CurrencyConverterCalculatorDatabase
import com.russhwolf.settings.JsSettings
import com.russhwolf.settings.Settings
import org.koin.core.module.Module

actual fun Module.getSettingsDefinition() = single<Settings> {
    JsSettings(get())
}

actual fun Module.getDatabaseDefinition(databaseName: String) = single {
    // todo JS SqlDelight is not ready yet. https://github.com/cashapp/sqldelight/issues/1667
    @Suppress("CAST_NEVER_SUCCEEDS")
    null as CurrencyConverterCalculatorDatabase
}
