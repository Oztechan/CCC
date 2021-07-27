package com.github.mustafaozhan.ccc.client.di.module

import android.content.Context
import android.content.SharedPreferences
import org.koin.dsl.module

private const val KEY_APPLICATION_PREFERENCES = "application_preferences"

fun getAndroidModule(context: Context) = module {
    single { context }
    single { provideSharedPreferences(get()) }
}

private fun provideSharedPreferences(
    context: Context
): SharedPreferences = context.getSharedPreferences(
    KEY_APPLICATION_PREFERENCES,
    Context.MODE_PRIVATE
)
