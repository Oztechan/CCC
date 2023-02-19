package com.oztechan.ccc.backend.service.premium.di

import com.oztechan.ccc.backend.service.premium.PremiumApiService
import com.oztechan.ccc.backend.service.premium.PremiumApiServiceImpl
import com.oztechan.ccc.common.core.infrastructure.di.DISPATCHER_IO
import org.koin.core.qualifier.named
import org.koin.dsl.module

val backendServicePremiumModule = module {
    single<PremiumApiService> { PremiumApiServiceImpl(get(), get(named(DISPATCHER_IO))) }
}
