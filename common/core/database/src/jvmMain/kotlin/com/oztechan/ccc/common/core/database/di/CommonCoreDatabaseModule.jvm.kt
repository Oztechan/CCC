package com.oztechan.ccc.common.core.database.di

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.oztechan.ccc.common.core.database.sql.CurrencyConverterCalculatorDatabase
import org.koin.core.scope.Scope

actual fun Scope.provideDatabase(databaseName: String) = CurrencyConverterCalculatorDatabase(
    JdbcSqliteDriver("jdbc:sqlite:$databaseName.db")
        .also { CurrencyConverterCalculatorDatabase.Schema.create(it) }
)
