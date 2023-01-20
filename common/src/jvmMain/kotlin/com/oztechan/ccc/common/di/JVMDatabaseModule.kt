package com.oztechan.ccc.common.di

import com.oztechan.ccc.common.database.sql.CurrencyConverterCalculatorDatabase
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.koin.core.scope.Scope

actual fun Scope.provideDatabase(databaseName: String) = CurrencyConverterCalculatorDatabase(
    JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        .also { CurrencyConverterCalculatorDatabase.Schema.create(it) }
)
