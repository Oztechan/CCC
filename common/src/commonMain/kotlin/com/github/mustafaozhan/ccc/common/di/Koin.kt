/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.di

import co.touchlab.kermit.Kermit
import com.github.mustafaozhan.ccc.common.CurrencyConverterCalculatorDatabase
import com.github.mustafaozhan.ccc.common.data.api.ApiFactory
import com.github.mustafaozhan.ccc.common.data.api.ApiRepository
import com.github.mustafaozhan.ccc.common.data.db.CurrencyDao
import com.github.mustafaozhan.ccc.common.data.db.OfflineRatesDao
import com.github.mustafaozhan.ccc.common.data.settings.SettingsRepository
import com.github.mustafaozhan.ccc.common.fake.FakeCurrencyQueries
import com.github.mustafaozhan.ccc.common.fake.FakeOfflineRatesQueries
import com.github.mustafaozhan.ccc.common.getPlatformCommonModule
import com.github.mustafaozhan.ccc.common.log.kermit
import com.github.mustafaozhan.logmob.LogMobLogger
import kotlin.reflect.KClass
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

const val DATABASE_NAME = "application_database.sqlite"

fun initCommon(
    clientModule: List<Module> = emptyList(),
    useFakes: Boolean = false
) = startKoin {
    modules(clientModule)
    modules(
        getPlatformCommonModule(useFakes),
        getCommonModule(useFakes)
    )
}.also {
    kermit.d { "Koin initCommon" }
}

fun getCommonModule(useFakes: Boolean): Module = module {
    single { Kermit(LogMobLogger()) }
    single { SettingsRepository(get()) }

    factory { ApiFactory() }
    single { ApiRepository(get()) }

    if (useFakes) {
        single { FakeCurrencyQueries.getCurrencyQueries() }
        single { FakeOfflineRatesQueries.getOfflineRatesQueries() }
    } else {
        single { get<CurrencyConverterCalculatorDatabase>().currencyQueries }
        single { get<CurrencyConverterCalculatorDatabase>().offlineRatesQueries }
    }

    single { CurrencyDao(get()) }
    single { OfflineRatesDao(get()) }
}

fun <T> Koin.getDependency(clazz: KClass<*>): T {
    return get(clazz, null) { parametersOf(clazz.simpleName) } as T
}
