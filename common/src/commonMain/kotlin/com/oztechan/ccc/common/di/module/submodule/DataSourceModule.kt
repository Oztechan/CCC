package com.oztechan.ccc.common.di.module.submodule

import com.oztechan.ccc.common.datasource.conversion.ConversionDataSource
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSourceImpl
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSourceImpl
import com.oztechan.ccc.common.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.common.datasource.watcher.WatcherDataSourceImpl
import com.oztechan.ccc.common.di.DISPATCHER_IO
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val dataSourceModule = module {
    single<CurrencyDataSource> { CurrencyDataSourceImpl(get(), get(named(DISPATCHER_IO))) }
    single<ConversionDataSource> { ConversionDataSourceImpl(get(), get(named(DISPATCHER_IO))) }
    single<WatcherDataSource> { WatcherDataSourceImpl(get(), get(named(DISPATCHER_IO))) }
}
