package com.oztechan.ccc.common.data.service.free.di

import com.oztechan.ccc.common.core.infrastructure.di.DISPATCHER_IO
import com.oztechan.ccc.common.data.service.free.FreeApiService
import com.oztechan.ccc.common.data.service.free.FreeApiServiceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val commonDataServiceFreeModule = module {
    single<FreeApiService> { FreeApiServiceImpl(get(), get(named(DISPATCHER_IO))) }
}
