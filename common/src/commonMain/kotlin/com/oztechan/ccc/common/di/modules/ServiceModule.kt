package com.oztechan.ccc.common.di.modules

import com.oztechan.ccc.common.di.DISPATCHER_IO
import com.oztechan.ccc.common.service.backend.BackendApiService
import com.oztechan.ccc.common.service.backend.BackendApiServiceImpl
import com.oztechan.ccc.common.service.free.FreeApiService
import com.oztechan.ccc.common.service.free.FreeApiServiceImpl
import com.oztechan.ccc.common.service.premium.PremiumApiService
import com.oztechan.ccc.common.service.premium.PremiumApiServiceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val serviceModule = module {
    single<FreeApiService> { FreeApiServiceImpl(get(), get(named(DISPATCHER_IO))) }
    single<BackendApiService> { BackendApiServiceImpl(get(), get(named(DISPATCHER_IO))) }
    single<PremiumApiService> { PremiumApiServiceImpl(get(), get(named(DISPATCHER_IO))) }
}
