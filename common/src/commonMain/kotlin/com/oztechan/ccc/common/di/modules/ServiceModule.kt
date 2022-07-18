package com.oztechan.ccc.common.di.modules

import com.oztechan.ccc.common.di.DISPATCHER_IO
import com.oztechan.ccc.common.service.free.FreeApiService
import com.oztechan.ccc.common.service.free.FreeApiServiceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val serviceModule = module {
    single<FreeApiService> { FreeApiServiceImpl(get(), get(named(DISPATCHER_IO))) }
}