package com.oztechan.ccc.common.data.datasource.watcher.di

import com.oztechan.ccc.common.core.infrastructure.di.DISPATCHER_IO
import com.oztechan.ccc.common.data.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.common.data.datasource.watcher.WatcherDataSourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val commonDataDatasourceWatcherModule = module {
    single<WatcherDataSource> { WatcherDataSourceImpl(get(), get(named(DISPATCHER_IO))) }
}
