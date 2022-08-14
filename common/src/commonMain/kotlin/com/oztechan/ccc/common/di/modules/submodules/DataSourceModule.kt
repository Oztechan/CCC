package com.oztechan.ccc.common.di.modules.submodules

import com.oztechan.ccc.common.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSourceImpl
import com.oztechan.ccc.common.datasource.offlinerates.OfflineRatesDataSource
import com.oztechan.ccc.common.datasource.offlinerates.OfflineRatesDataSourceImpl
import com.oztechan.ccc.common.datasource.settings.SettingsDataSource
import com.oztechan.ccc.common.datasource.settings.SettingsDataSourceImp
import com.oztechan.ccc.common.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.common.datasource.watcher.WatcherDataSourceImpl
import com.oztechan.ccc.common.di.DISPATCHER_IO
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val dataSourceModule = module {
    singleOf(::SettingsDataSourceImp) { bind<SettingsDataSource>() }

    single<CurrencyDataSource> { CurrencyDataSourceImpl(get(), get(named(DISPATCHER_IO))) }
    single<OfflineRatesDataSource> { OfflineRatesDataSourceImpl(get(), get(named(DISPATCHER_IO))) }
    single<WatcherDataSource> { WatcherDataSourceImpl(get(), get(named(DISPATCHER_IO))) }
}
