package com.oztechan.ccc.client.viewmodel.widget.di

import com.oztechan.ccc.client.viewmodel.widget.WidgetViewModel
import org.koin.dsl.module

val clientViewModelWidgetModule = module {
    // widget viewModel has to be singleton
    single { WidgetViewModel(get(), get(), get(), get()) }
}
