/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.di

import com.github.mustafaozhan.ccc.common.CurrencyConverterCalculatorDatabase
import com.github.mustafaozhan.ccc.common.api.ApiFactory
import com.github.mustafaozhan.ccc.common.api.ApiRepository
import com.github.mustafaozhan.ccc.common.db.CurrencyDao
import com.github.mustafaozhan.ccc.common.db.OfflineRatesDao
import com.github.mustafaozhan.ccc.common.kermit
import com.github.mustafaozhan.ccc.common.platformCommonModule
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import kotlin.reflect.KClass
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

const val DATABASE_NAME = "application_database.sqlite"

fun initCommon(clientModule: List<Module> = emptyList()) = startKoin {
    modules(clientModule)
    modules(platformCommonModule, commonModule)
}.also {
    kermit.d { "Koin initCommon" }
}

var commonModule: Module = module {
    single { SettingsRepository(get()) }

    factory { ApiFactory() }
    single { ApiRepository(get()) }

    single { get<CurrencyConverterCalculatorDatabase>().currencyQueries }
    single { get<CurrencyConverterCalculatorDatabase>().offlineRatesQueries }
    single { CurrencyDao(get()) }
    single { OfflineRatesDao(get()) }
}

fun <T> Koin.getDependency(clazz: KClass<*>): T {
    return get(clazz, null) { parametersOf(clazz.simpleName) } as T
}
