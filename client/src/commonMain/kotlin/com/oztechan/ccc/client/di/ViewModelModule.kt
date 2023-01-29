package com.oztechan.ccc.client.di

import com.oztechan.ccc.client.viewmodel.calculator.CalculatorViewModel
import com.oztechan.ccc.client.viewmodel.currencies.CurrenciesViewModel
import com.oztechan.ccc.client.viewmodel.di.viewModelDefinition
import com.oztechan.ccc.client.viewmodel.main.MainViewModel
import com.oztechan.ccc.client.viewmodel.premium.PremiumViewModel
import com.oztechan.ccc.client.viewmodel.selectcurrency.SelectCurrencyViewModel
import com.oztechan.ccc.client.viewmodel.settings.SettingsViewModel
import com.oztechan.ccc.client.viewmodel.watchers.WatchersViewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModelDefinition { SettingsViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModelDefinition { MainViewModel(get(), get(), get(), get(), get(), get()) }
    viewModelDefinition { CurrenciesViewModel(get(), get(), get(), get(), get()) }
    viewModelDefinition { CalculatorViewModel(get(), get(), get(), get(), get(), get()) }
    viewModelDefinition { SelectCurrencyViewModel(get()) }
    viewModelDefinition { PremiumViewModel(get()) }
    viewModelDefinition { WatchersViewModel(get(), get(), get()) }
}
