package com.oztechan.ccc.client.di.module

import android.content.Context
import android.content.SharedPreferences
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private const val KEY_APPLICATION_PREFERENCES = "application_preferences"

val androidModule = module {
    singleOf(::provideSharedPreferences)
}

private fun provideSharedPreferences(
    context: Context
): SharedPreferences = context.getSharedPreferences(
    KEY_APPLICATION_PREFERENCES,
    Context.MODE_PRIVATE
)
