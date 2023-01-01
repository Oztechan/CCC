package com.oztechan.ccc.common.di.module.submodule

import com.oztechan.ccc.common.database.sql.CurrencyConverterCalculatorDatabase
import org.koin.core.scope.Scope
import org.koin.dsl.module

private const val DATABASE_NAME = "application_database.sqlite"

internal val databaseModule = module {
    single { get<CurrencyConverterCalculatorDatabase>().currencyQueries }
    single { get<CurrencyConverterCalculatorDatabase>().conversionQueries }
    single { get<CurrencyConverterCalculatorDatabase>().watcherQueries }

    single { provideDatabase(DATABASE_NAME) }
}

expect fun Scope.provideDatabase(databaseName: String): CurrencyConverterCalculatorDatabase
