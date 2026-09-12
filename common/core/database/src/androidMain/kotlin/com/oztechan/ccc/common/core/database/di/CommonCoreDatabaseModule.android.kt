package com.oztechan.ccc.common.core.database.di

import com.oztechan.ccc.common.core.database.sql.CurrencyConverterCalculatorDatabase
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.koin.core.scope.Scope

actual fun Scope.provideDatabase(databaseName: String) = CurrencyConverterCalculatorDatabase(
    AndroidSqliteDriver(
        CurrencyConverterCalculatorDatabase.Schema,
        get(),
        databaseName
    )
)
