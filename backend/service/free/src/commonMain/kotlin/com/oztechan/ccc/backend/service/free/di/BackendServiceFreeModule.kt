package com.oztechan.ccc.backend.service.free.di

import com.oztechan.ccc.backend.service.free.FreeApiService
import com.oztechan.ccc.backend.service.free.FreeApiServiceImpl
import com.oztechan.ccc.common.core.infrastructure.di.DISPATCHER_IO
import org.koin.core.qualifier.named
import org.koin.dsl.module

val backendServiceFreeModule = module {
    single<FreeApiService> { FreeApiServiceImpl(get(), get(named(DISPATCHER_IO))) }
}
