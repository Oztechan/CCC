package com.oztechan.ccc.common.di.modules

import com.oztechan.ccc.common.db.offlinerates.OfflineRatesRepository
import com.oztechan.ccc.common.db.offlinerates.OfflineRatesRepositoryImpl
import com.oztechan.ccc.common.db.sql.CurrencyConverterCalculatorDatabase
import com.oztechan.ccc.common.db.watcher.WatcherRepository
import com.oztechan.ccc.common.db.watcher.WatcherRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.scope.Scope
import org.koin.dsl.module

private const val DATABASE_NAME = "application_database.sqlite"

val databaseModule = module {
    single { get<CurrencyConverterCalculatorDatabase>().currencyQueries }
    single { get<CurrencyConverterCalculatorDatabase>().offlineRatesQueries }
    single { get<CurrencyConverterCalculatorDatabase>().watcherQueries }

    singleOf(::OfflineRatesRepositoryImpl) { bind<OfflineRatesRepository>() }
    singleOf(::WatcherRepositoryImpl) { bind<WatcherRepository>() }

    single { provideDatabase(DATABASE_NAME) }
}

expect fun Scope.provideDatabase(databaseName: String): CurrencyConverterCalculatorDatabase
