package com.oztechan.ccc.common.di

import com.oztechan.ccc.common.core.infrastructure.di.DISPATCHER_IO
import com.oztechan.ccc.common.service.backend.BackendApiService
import com.oztechan.ccc.common.service.backend.BackendApiServiceImpl
import com.oztechan.ccc.common.service.premium.PremiumApiService
import com.oztechan.ccc.common.service.premium.PremiumApiServiceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val serviceModule = module {
    single<BackendApiService> { BackendApiServiceImpl(get(), get(named(DISPATCHER_IO))) }
    single<PremiumApiService> { PremiumApiServiceImpl(get(), get(named(DISPATCHER_IO))) }
}
