package com.github.mustafaozhan.ccc.common.di.modules

import com.github.mustafaozhan.ccc.common.db.currency.CurrencyRepository
import com.github.mustafaozhan.ccc.common.db.currency.CurrencyRepositoryImpl
import com.github.mustafaozhan.ccc.common.db.offlinerates.OfflineRatesRepository
import com.github.mustafaozhan.ccc.common.db.offlinerates.OfflineRatesRepositoryImpl
import com.github.mustafaozhan.ccc.common.db.sql.CurrencyConverterCalculatorDatabase
import org.koin.core.scope.Scope
import org.koin.dsl.module

private const val DATABASE_NAME = "application_database.sqlite"

fun getDatabaseModule() = module {
    single { get<CurrencyConverterCalculatorDatabase>().currencyQueries }
    single { get<CurrencyConverterCalculatorDatabase>().offlineRatesQueries }

    single<CurrencyRepository> { CurrencyRepositoryImpl(get()) }
    single<OfflineRatesRepository> { OfflineRatesRepositoryImpl(get()) }

    single { provideDatabase(DATABASE_NAME) }
}

expect fun Scope.provideDatabase(databaseName: String): CurrencyConverterCalculatorDatabase
