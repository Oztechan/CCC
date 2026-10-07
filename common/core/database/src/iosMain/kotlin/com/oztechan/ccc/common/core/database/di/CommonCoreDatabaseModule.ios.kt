package com.oztechan.ccc.common.core.database.di

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.oztechan.ccc.common.core.database.sql.CurrencyConverterCalculatorDatabase
import org.koin.core.scope.Scope

actual fun Scope.provideDatabase(databaseName: String) = CurrencyConverterCalculatorDatabase(
    NativeSqliteDriver(
        CurrencyConverterCalculatorDatabase.Schema,
        databaseName
    )
)
