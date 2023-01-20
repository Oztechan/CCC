package com.oztechan.ccc.common.di

import com.oztechan.ccc.common.database.sql.CurrencyConverterCalculatorDatabase
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import org.koin.core.scope.Scope

actual fun Scope.provideDatabase(databaseName: String) = CurrencyConverterCalculatorDatabase(
    NativeSqliteDriver(
        CurrencyConverterCalculatorDatabase.Schema,
        databaseName
    )
)
