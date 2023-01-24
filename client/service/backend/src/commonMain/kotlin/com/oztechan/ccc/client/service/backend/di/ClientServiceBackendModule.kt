package com.oztechan.ccc.client.service.backend.di

import com.oztechan.ccc.client.service.backend.BackendApiService
import com.oztechan.ccc.client.service.backend.BackendApiServiceImpl
import com.oztechan.ccc.common.core.infrastructure.di.DISPATCHER_IO
import org.koin.core.qualifier.named
import org.koin.dsl.module

val clientServiceBackendModule = module {
    single<BackendApiService> { BackendApiServiceImpl(get(), get(named(DISPATCHER_IO))) }
}
