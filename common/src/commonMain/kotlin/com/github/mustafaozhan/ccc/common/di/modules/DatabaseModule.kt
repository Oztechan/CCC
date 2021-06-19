package com.github.mustafaozhan.ccc.common.di.modules

import com.github.mustafaozhan.ccc.common.db.currency.CurrencyRepository
import com.github.mustafaozhan.ccc.common.db.currency.CurrencyRepositoryImpl
import com.github.mustafaozhan.ccc.common.db.offlinerates.OfflineRatesRepository
import com.github.mustafaozhan.ccc.common.db.offlinerates.OfflineRatesRepositoryImpl
import com.github.mustafaozhan.ccc.common.db.sql.CurrencyConverterCalculatorDatabase
import com.github.mustafaozhan.ccc.common.di.getDatabaseDefinition
import com.github.mustafaozhan.ccc.common.fake.FakeCurrencyQueries
import com.github.mustafaozhan.ccc.common.fake.FakeOfflineRatesQueries
import org.koin.dsl.module

private const val DATABASE_NAME = "application_database.sqlite"

fun getDatabaseModule(forTest: Boolean = false) = module {
    if (forTest) {
        single { FakeCurrencyQueries.getCurrencyQueries() }
        single { FakeOfflineRatesQueries.getOfflineRatesQueries() }
    } else {
        single { get<CurrencyConverterCalculatorDatabase>().currencyQueries }
        single { get<CurrencyConverterCalculatorDatabase>().offlineRatesQueries }
    }

    single<CurrencyRepository> { CurrencyRepositoryImpl(get()) }
    single<OfflineRatesRepository> { OfflineRatesRepositoryImpl(get()) }

    getDatabaseDefinition(DATABASE_NAME)
}
