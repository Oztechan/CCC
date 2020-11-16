/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.di

import com.github.mustafaozhan.ccc.client.main.MainViewModel
import com.russhwolf.settings.JsSettings
import com.russhwolf.settings.Settings
import kotlin.reflect.KClass
import org.koin.core.Koin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.w3c.dom.Storage

fun initJS(storage: Storage) = initKoin(
    module {
        single<Settings> { JsSettings(storage) }
    }
)

actual val platformClientModule: Module = module {
    single { MainViewModel(get(), get()) }
}

fun <T> Koin.getForJs(clazz: KClass<*>): T {
    return get(clazz, null) { parametersOf(clazz.simpleName) } as T
}
