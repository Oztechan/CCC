package com.oztechan.ccc.client.di.module

import com.oztechan.ccc.client.di.viewModelDefinition
import com.oztechan.ccc.client.helper.SessionManager
import com.oztechan.ccc.client.helper.SessionManagerImpl
import com.oztechan.ccc.client.viewmodel.adremove.AdRemoveViewModel
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorViewModel
import com.oztechan.ccc.client.viewmodel.currencies.CurrenciesViewModel
import com.oztechan.ccc.client.viewmodel.main.MainViewModel
import com.oztechan.ccc.client.viewmodel.selectcurrency.SelectCurrencyViewModel
import com.oztechan.ccc.client.viewmodel.settings.SettingsViewModel
import com.oztechan.ccc.config.ConfigManager
import com.oztechan.ccc.config.ConfigManagerImpl
import org.koin.dsl.module

var clientModule = module {
    viewModelDefinition { SettingsViewModel(get(), get(), get(), get(), get()) }
    viewModelDefinition { MainViewModel(get(), get(), get()) }
    viewModelDefinition { CurrenciesViewModel(get(), get(), get()) }
    viewModelDefinition { CalculatorViewModel(get(), get(), get(), get(), get()) }
    viewModelDefinition { SelectCurrencyViewModel(get()) }
    viewModelDefinition { AdRemoveViewModel(get()) }

    single<ConfigManager> { ConfigManagerImpl() }
    single<SessionManager> { SessionManagerImpl(get(), get()) }
}
