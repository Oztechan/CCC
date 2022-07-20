package com.oztechan.ccc.client.di.module

import com.oztechan.ccc.client.di.viewModelDefinition
import com.oztechan.ccc.client.repository.background.BackgroundRepository
import com.oztechan.ccc.client.repository.background.BackgroundRepositoryImpl
import com.oztechan.ccc.client.repository.session.SessionRepository
import com.oztechan.ccc.client.repository.session.SessionRepositoryImpl
import com.oztechan.ccc.client.viewmodel.adremove.AdRemoveViewModel
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorViewModel
import com.oztechan.ccc.client.viewmodel.currencies.CurrenciesViewModel
import com.oztechan.ccc.client.viewmodel.main.MainViewModel
import com.oztechan.ccc.client.viewmodel.selectcurrency.SelectCurrencyViewModel
import com.oztechan.ccc.client.viewmodel.settings.SettingsViewModel
import com.oztechan.ccc.client.viewmodel.watchers.WatchersViewModel
import com.oztechan.ccc.config.ConfigService
import com.oztechan.ccc.config.ConfigServiceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

var clientModule = module {
    viewModelDefinition { SettingsViewModel(get(), get(), get(), get(), get(), get()) }
    viewModelDefinition { MainViewModel(get(), get(), get()) }
    viewModelDefinition { CurrenciesViewModel(get(), get(), get()) }
    viewModelDefinition { CalculatorViewModel(get(), get(), get(), get(), get()) }
    viewModelDefinition { SelectCurrencyViewModel(get()) }
    viewModelDefinition { AdRemoveViewModel(get()) }
    viewModelDefinition { WatchersViewModel(get(), get()) }

    singleOf(::ConfigServiceImpl) { bind<ConfigService>() }
    singleOf(::SessionRepositoryImpl) { bind<SessionRepository>() }
    singleOf(::BackgroundRepositoryImpl) { bind<BackgroundRepository>() }
}
