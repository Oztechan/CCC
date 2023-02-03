package com.oztechan.ccc.client.viewmodel.main.di

import com.oztechan.ccc.client.core.viewmodel.di.viewModelDefinition
import com.oztechan.ccc.client.viewmodel.main.MainViewModel
import org.koin.dsl.module

val clientViewModelMainModule = module {
    viewModelDefinition { MainViewModel(get(), get(), get(), get(), get(), get()) }
}
