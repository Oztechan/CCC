package com.oztechan.ccc.client.viewmodel.settings.di

import com.oztechan.ccc.client.core.viewmodel.di.viewModelDefinition
import com.oztechan.ccc.client.viewmodel.settings.SettingsViewModel
import org.koin.dsl.module

val clientViewModelSettingsModule = module {
    viewModelDefinition { SettingsViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
}
