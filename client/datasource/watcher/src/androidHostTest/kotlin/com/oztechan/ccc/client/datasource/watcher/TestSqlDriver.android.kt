package com.oztechan.ccc.client.datasource.watcher

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.oztechan.ccc.common.core.database.sql.CurrencyConverterCalculatorDatabase

internal actual fun createTestSqlDriver(): SqlDriver =
    JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        .also { CurrencyConverterCalculatorDatabase.Schema.create(it) }
