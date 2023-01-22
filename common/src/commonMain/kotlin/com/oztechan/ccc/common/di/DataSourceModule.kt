package com.oztechan.ccc.common.di

import com.oztechan.ccc.common.core.infrastructure.di.DISPATCHER_IO
import com.oztechan.ccc.common.datasource.exchangerate.ExchangeRateDataSource
import com.oztechan.ccc.common.datasource.exchangerate.ExchangeRateDataSourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataSourceModule = module {
    single<ExchangeRateDataSource> { ExchangeRateDataSourceImpl(get(), get(named(DISPATCHER_IO))) }
}
