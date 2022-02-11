package com.github.mustafaozhan.ccc.client.di.module

import com.github.mustafaozhan.ccc.client.di.viewModelDefinition
import com.github.mustafaozhan.ccc.client.util.SessionManager
import com.github.mustafaozhan.ccc.client.viewmodel.adremove.AdRemoveViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.changebase.ChangeBaseViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.currencies.CurrenciesViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.main.MainViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.settings.SettingsViewModel
import com.github.mustafaozhan.config.ConfigManager
import com.github.mustafaozhan.config.ConfigManagerImpl
import org.koin.dsl.module

var clientModule = module {
    viewModelDefinition { SettingsViewModel(get(), get(), get(), get(), get()) }
    viewModelDefinition { MainViewModel(get(), get(), get()) }
    viewModelDefinition { CurrenciesViewModel(get(), get(), get()) }
    viewModelDefinition { CalculatorViewModel(get(), get(), get(), get(), get()) }
    viewModelDefinition { ChangeBaseViewModel(get()) }
    viewModelDefinition { AdRemoveViewModel(get()) }

    single<ConfigManager> { ConfigManagerImpl() }
    single { SessionManager(get(), get()) }
}
