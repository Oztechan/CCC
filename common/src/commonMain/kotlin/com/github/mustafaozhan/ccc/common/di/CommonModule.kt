package com.github.mustafaozhan.ccc.common.di

import com.github.mustafaozhan.ccc.common.api.ApiFactory
import com.github.mustafaozhan.ccc.common.api.ApiRepository
import com.github.mustafaozhan.ccc.common.db.dao.CurrencyDao
import com.github.mustafaozhan.ccc.common.db.dao.OfflineRatesDao
import com.github.mustafaozhan.ccc.common.db.sql.CurrencyConverterCalculatorDatabase
import com.github.mustafaozhan.ccc.common.fake.FakeCurrencyQueries
import com.github.mustafaozhan.ccc.common.fake.FakeOfflineRatesQueries
import com.github.mustafaozhan.ccc.common.fake.FakeSettings
import com.github.mustafaozhan.ccc.common.getDatabaseDefinition
import com.github.mustafaozhan.ccc.common.getSettingsDefinition
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import org.koin.core.module.Module
import org.koin.dsl.module

fun getCommonModule(forTest: Boolean): Module = module {
    factory { ApiFactory() }
    single { ApiRepository(get()) }

    getDatabaseDefinition()

    if (forTest) {
        single { FakeSettings.getSettings() }
        single { FakeCurrencyQueries.getCurrencyQueries() }
        single { FakeOfflineRatesQueries.getOfflineRatesQueries() }
    } else {
        getSettingsDefinition()
        single { get<CurrencyConverterCalculatorDatabase>().currencyQueries }
        single { get<CurrencyConverterCalculatorDatabase>().offlineRatesQueries }
    }

    single { SettingsRepository(get()) }

    single { CurrencyDao(get()) }
    single { OfflineRatesDao(get()) }
}
