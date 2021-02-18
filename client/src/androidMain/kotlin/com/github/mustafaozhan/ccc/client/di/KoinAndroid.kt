/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.di

import android.content.Context
import android.content.SharedPreferences
import com.github.mustafaozhan.ccc.client.base.BaseViewModel
import com.github.mustafaozhan.ccc.common.log.kermit
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.KoinApplication
import org.koin.core.definition.BeanDefinition
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module

private const val KEY_APPLICATION_PREFERENCES = "application_preferences"

fun initAndroid(context: Context): KoinApplication = initClient(
    module {
        single { context }
        single<SharedPreferences> {
            context.getSharedPreferences(
                KEY_APPLICATION_PREFERENCES,
                Context.MODE_PRIVATE
            )
        }
    }
).also {
    kermit.d { "KoinAndroid initAndroid" }
}

actual inline fun <reified T : BaseViewModel> Module.viewModelDefinition(
    qualifier: Qualifier?,
    override: Boolean,
    noinline definition: Definition<T>
): BeanDefinition<T> = viewModel(qualifier, override, definition)
