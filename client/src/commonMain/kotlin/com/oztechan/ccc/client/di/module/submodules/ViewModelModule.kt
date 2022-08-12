package com.oztechan.ccc.client.di.module.submodules

import com.oztechan.ccc.client.base.BaseViewModel
import com.oztechan.ccc.client.viewmodel.adremove.AdRemoveViewModel
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorViewModel
import com.oztechan.ccc.client.viewmodel.currencies.CurrenciesViewModel
import com.oztechan.ccc.client.viewmodel.main.MainViewModel
import com.oztechan.ccc.client.viewmodel.selectcurrency.SelectCurrencyViewModel
import com.oztechan.ccc.client.viewmodel.settings.SettingsViewModel
import com.oztechan.ccc.client.viewmodel.watchers.WatchersViewModel
import org.koin.core.definition.Definition
import org.koin.core.instance.InstanceFactory
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module

internal val viewModelModule = module {
    viewModelDefinition { SettingsViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModelDefinition { MainViewModel(get(), get(), get(), get()) }
    viewModelDefinition { CurrenciesViewModel(get(), get(), get(), get()) }
    viewModelDefinition { CalculatorViewModel(get(), get(), get(), get(), get(), get()) }
    viewModelDefinition { SelectCurrencyViewModel(get()) }
    viewModelDefinition { AdRemoveViewModel(get()) }
    viewModelDefinition { WatchersViewModel(get(), get()) }
}

expect inline fun <reified T : BaseViewModel> Module.viewModelDefinition(
    qualifier: Qualifier? = null,
    createdAtStart: Boolean = false,
    noinline definition: Definition<T>
): Pair<Module, InstanceFactory<T>>
