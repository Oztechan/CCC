package com.github.mustafaozhan.ccc.client.di.module

import org.koin.dsl.module
import java.util.prefs.Preferences

fun getJvmModule(delegate: Preferences) = module {
    single { delegate }
}
