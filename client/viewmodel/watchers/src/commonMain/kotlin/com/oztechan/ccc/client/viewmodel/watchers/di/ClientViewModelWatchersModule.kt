package com.oztechan.ccc.client.viewmodel.watchers.di

import com.oztechan.ccc.client.viewmodel.di.viewModelDefinition
import com.oztechan.ccc.client.viewmodel.watchers.WatchersViewModel
import org.koin.dsl.module

val clientViewModelWatchersModule = module {
    // widget viewModel has to be singleton
    viewModelDefinition { WatchersViewModel(get(), get(), get()) }
}
