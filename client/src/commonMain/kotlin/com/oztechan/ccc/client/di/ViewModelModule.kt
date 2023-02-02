package com.oztechan.ccc.client.di

import com.oztechan.ccc.client.core.viewmodel.di.viewModelDefinition
import com.oztechan.ccc.client.viewmodel.main.MainViewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModelDefinition { MainViewModel(get(), get(), get(), get(), get(), get()) }
}
