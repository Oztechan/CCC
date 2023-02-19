package com.oztechan.ccc.client.viewmodel.currencies.di

import com.oztechan.ccc.client.core.viewmodel.di.viewModelDefinition
import com.oztechan.ccc.client.viewmodel.currencies.CurrenciesViewModel
import org.koin.dsl.module

val clientViewModelCurrenciesModule = module {
    viewModelDefinition { CurrenciesViewModel(get(), get(), get(), get(), get()) }
}
