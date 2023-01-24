package com.oztechan.ccc.client.datasource.watcher.di

import com.oztechan.ccc.client.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.client.datasource.watcher.WatcherDataSourceImpl
import com.oztechan.ccc.common.core.infrastructure.di.DISPATCHER_IO
import org.koin.core.qualifier.named
import org.koin.dsl.module

val clientDataSourceWatcherModule = module {
    single<WatcherDataSource> { WatcherDataSourceImpl(get(), get(named(DISPATCHER_IO))) }
}
