package com.oztechan.ccc.common.datasource.conversion.di

import com.oztechan.ccc.common.core.infrastructure.di.DISPATCHER_IO
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSource
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val commonDataDatasourceConversionModule = module {
    single<ConversionDataSource> { ConversionDataSourceImpl(get(), get(named(DISPATCHER_IO))) }
}
