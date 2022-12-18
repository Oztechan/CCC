package com.oztechan.ccc.common.di.module.submodule

import com.oztechan.ccc.common.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSourceImpl
import com.oztechan.ccc.common.datasource.rates.RatesDataSource
import com.oztechan.ccc.common.datasource.rates.RatesDataSourceImpl
import com.oztechan.ccc.common.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.common.datasource.watcher.WatcherDataSourceImpl
import com.oztechan.ccc.common.di.DISPATCHER_IO
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val dataSourceModule = module {
    single<CurrencyDataSource> { CurrencyDataSourceImpl(get(), get(named(DISPATCHER_IO))) }
    single<RatesDataSource> { RatesDataSourceImpl(get(), get(named(DISPATCHER_IO))) }
    single<WatcherDataSource> { WatcherDataSourceImpl(get(), get(named(DISPATCHER_IO))) }
}
