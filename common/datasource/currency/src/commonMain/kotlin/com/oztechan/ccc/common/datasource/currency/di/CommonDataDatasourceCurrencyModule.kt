package com.oztechan.ccc.common.datasource.currency.di

import com.oztechan.ccc.common.core.infrastructure.di.DISPATCHER_IO
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val commonDataDatasourceCurrencyModule = module {
    single<CurrencyDataSource> { CurrencyDataSourceImpl(get(), get(named(DISPATCHER_IO))) }
}
