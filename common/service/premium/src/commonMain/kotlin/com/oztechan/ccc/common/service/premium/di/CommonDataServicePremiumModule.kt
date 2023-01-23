package com.oztechan.ccc.common.service.premium.di

import com.oztechan.ccc.common.core.infrastructure.di.DISPATCHER_IO
import com.oztechan.ccc.common.service.premium.PremiumApiService
import com.oztechan.ccc.common.service.premium.PremiumApiServiceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val commonDataServicePremiumModule = module {
    single<PremiumApiService> { PremiumApiServiceImpl(get(), get(named(DISPATCHER_IO))) }
}
