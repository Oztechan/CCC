package com.oztechan.ccc.common.core.database.di

import com.oztechan.ccc.common.core.database.sql.CurrencyConverterCalculatorDatabase
import org.koin.core.scope.Scope
import org.koin.dsl.module

private const val DATABASE_NAME = "application_database.sqlite"

val commonCoreDatabaseModule = module {
    single { get<CurrencyConverterCalculatorDatabase>().currencyQueries }
    single { get<CurrencyConverterCalculatorDatabase>().conversionQueries }
    single { get<CurrencyConverterCalculatorDatabase>().watcherQueries }

    single { provideDatabase(DATABASE_NAME) }
}

expect fun Scope.provideDatabase(databaseName: String): CurrencyConverterCalculatorDatabase
