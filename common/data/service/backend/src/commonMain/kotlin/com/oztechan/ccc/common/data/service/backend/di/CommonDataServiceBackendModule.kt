package com.oztechan.ccc.common.data.service.backend.di

import com.oztechan.ccc.common.core.infrastructure.di.DISPATCHER_IO
import com.oztechan.ccc.common.data.service.backend.BackendApiService
import com.oztechan.ccc.common.data.service.backend.BackendApiServiceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val commonDataServiceBackendModule = module {
    single<BackendApiService> { BackendApiServiceImpl(get(), get(named(DISPATCHER_IO))) }
}
