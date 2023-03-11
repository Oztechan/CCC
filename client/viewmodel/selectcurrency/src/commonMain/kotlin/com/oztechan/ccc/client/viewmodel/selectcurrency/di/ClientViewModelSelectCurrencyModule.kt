package com.oztechan.ccc.client.viewmodel.selectcurrency.di

import com.oztechan.ccc.client.core.viewmodel.di.viewModelDefinition
import com.oztechan.ccc.client.viewmodel.selectcurrency.SelectCurrencyViewModel
import org.koin.dsl.module

val clientViewModelSelectCurrencyModule = module {
    viewModelDefinition { SelectCurrencyViewModel(get()) }
}
