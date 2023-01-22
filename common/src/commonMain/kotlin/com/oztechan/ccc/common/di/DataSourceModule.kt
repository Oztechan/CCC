package com.oztechan.ccc.common.di

import com.oztechan.ccc.common.core.infrastructure.di.DISPATCHER_IO
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSource
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSourceImpl
import com.oztechan.ccc.common.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.common.datasource.watcher.WatcherDataSourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataSourceModule = module {
    single<ConversionDataSource> { ConversionDataSourceImpl(get(), get(named(DISPATCHER_IO))) }
    single<WatcherDataSource> { WatcherDataSourceImpl(get(), get(named(DISPATCHER_IO))) }
}
