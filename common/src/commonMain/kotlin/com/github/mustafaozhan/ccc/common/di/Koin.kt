/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

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
import com.github.mustafaozhan.logmob.initLogger
import com.github.mustafaozhan.logmob.kermit
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import kotlin.reflect.KClass

const val DATABASE_NAME = "application_database.sqlite"

fun initCommon(
    clientModule: List<Module> = emptyList(),
    forTest: Boolean = false
) = startKoin {
    modules(clientModule)
    modules(getCommonModule(forTest))
}.also {
    initLogger(forTest)
    kermit.d { "Koin initCommon" }
}

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

@Suppress("UNCHECKED_CAST")
fun <T> Koin.getDependency(clazz: KClass<*>): T {
    return get(clazz, null) { parametersOf(clazz.simpleName) } as T
}
