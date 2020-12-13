/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.di

import android.content.Context
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import kotlin.reflect.KClass
import org.koin.core.Koin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

private const val KEY_APPLICATION_PREFERENCES = "application_preferences"

fun initAndroid(androidModule: Module) = initKoin(androidModule)

actual val platformClientModule: Module = module {

    single<Settings> {
        AndroidSettings(
            get<Context>().getSharedPreferences(
                KEY_APPLICATION_PREFERENCES,
                Context.MODE_PRIVATE
            )
        )
    }
}

fun <T> Koin.getForAndroid(clazz: KClass<*>): T =
    get(clazz, null) { parametersOf(clazz.simpleName) } as T
