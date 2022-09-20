package com.oztechan.ccc.backend.di.modules.submodules

import com.oztechan.ccc.backend.repository.api.ApiRepository
import com.oztechan.ccc.backend.repository.api.ApiRepositoryImpl
import com.oztechan.ccc.common.di.DISPATCHER_IO
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val repositoryModule = module {
    single<ApiRepository> { ApiRepositoryImpl(get(), get(), get(), get(named(DISPATCHER_IO))) }
}
