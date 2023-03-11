package com.oztechan.ccc.client.datasource.currency.di

import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSourceImpl
import com.oztechan.ccc.common.core.infrastructure.di.DISPATCHER_IO
import org.koin.core.qualifier.named
import org.koin.dsl.module

val clientDataSourceCurrencyModule = module {
    single<CurrencyDataSource> { CurrencyDataSourceImpl(get(), get(named(DISPATCHER_IO))) }
}
