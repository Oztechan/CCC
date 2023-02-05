package com.oztechan.ccc.android.viewmodel.widget.di

import com.oztechan.ccc.android.viewmodel.widget.WidgetViewModel
import org.koin.dsl.module

val androidViewModelWidgetModule = module {
    // widget viewModel has to be singleton
    single {
        WidgetViewModel(
            calculationStorage = get(),
            backendApiService = get(),
            currencyDataSource = get(),
            appStorage = get()
        )
    }
}
