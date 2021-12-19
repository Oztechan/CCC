package com.github.mustafaozhan.ccc.common.di.modules

import com.github.mustafaozhan.ccc.common.db.sql.CurrencyConverterCalculatorDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import org.koin.core.scope.Scope

actual fun Scope.provideDatabase(databaseName: String) = CurrencyConverterCalculatorDatabase(
    AndroidSqliteDriver(
        CurrencyConverterCalculatorDatabase.Schema,
        get(),
        databaseName
    )
)
