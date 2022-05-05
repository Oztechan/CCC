package com.oztechan.ccc.common.di.modules

import com.oztechan.ccc.common.db.currency.CurrencyRepository
import com.oztechan.ccc.common.db.currency.CurrencyRepositoryImpl
import com.oztechan.ccc.common.db.notification.NotificationRepository
import com.oztechan.ccc.common.db.notification.NotificationRepositoryImpl
import com.oztechan.ccc.common.db.offlinerates.OfflineRatesRepository
import com.oztechan.ccc.common.db.offlinerates.OfflineRatesRepositoryImpl
import com.oztechan.ccc.common.db.sql.CurrencyConverterCalculatorDatabase
import org.koin.core.scope.Scope
import org.koin.dsl.module

private const val DATABASE_NAME = "application_database.sqlite"

fun getDatabaseModule() = module {
    single { get<CurrencyConverterCalculatorDatabase>().currencyQueries }
    single { get<CurrencyConverterCalculatorDatabase>().offlineRatesQueries }
    single { get<CurrencyConverterCalculatorDatabase>().notificationQueries }

    single<CurrencyRepository> { CurrencyRepositoryImpl(get()) }
    single<OfflineRatesRepository> { OfflineRatesRepositoryImpl(get()) }
    single<NotificationRepository> { NotificationRepositoryImpl(get()) }

    single { provideDatabase(DATABASE_NAME) }
}

expect fun Scope.provideDatabase(databaseName: String): CurrencyConverterCalculatorDatabase
