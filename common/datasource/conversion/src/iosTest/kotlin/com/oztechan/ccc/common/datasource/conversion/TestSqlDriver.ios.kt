package com.oztechan.ccc.common.datasource.conversion

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.inMemoryDriver
import com.oztechan.ccc.common.core.database.sql.CurrencyConverterCalculatorDatabase

internal actual fun createTestSqlDriver(): SqlDriver =
    inMemoryDriver(CurrencyConverterCalculatorDatabase.Schema)
