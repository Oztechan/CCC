/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.di

import com.github.mustafaozhan.ccc.client.base.BaseViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.BarViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.CalculatorViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.CurrenciesViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.MainViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.SettingsViewModel
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.di.initCommon
import com.github.mustafaozhan.logmob.kermit
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.definition.BeanDefinition
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module
import kotlin.reflect.KClass

fun initClient(appModule: Module, forTest: Boolean = false): KoinApplication = initCommon(
    appModule.plus(viewModelModule), forTest
).also {
    kermit.d { "Koin initClient" }
}

private val viewModelModule = module {
    viewModelDefinition { SettingsViewModel(get(), get(), get(), get()) }
    viewModelDefinition { MainViewModel(get()) }
    viewModelDefinition { CurrenciesViewModel(get(), get()) }
    viewModelDefinition { CalculatorViewModel(get(), get(), get(), get()) }
    viewModelDefinition { BarViewModel(get()) }
}

expect inline fun <reified T : BaseViewModel> Module.viewModelDefinition(
    qualifier: Qualifier? = null,
    override: Boolean = false,
    noinline definition: Definition<T>
): BeanDefinition<T>

fun <T> Koin.getDependency(clazz: KClass<*>) = getDependency<T>(clazz)
