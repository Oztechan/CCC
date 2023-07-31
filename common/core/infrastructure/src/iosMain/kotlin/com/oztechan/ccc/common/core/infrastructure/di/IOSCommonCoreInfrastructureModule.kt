package com.oztechan.ccc.common.core.infrastructure.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val commonCoreInfrastructureModule: Module = module {
    @Suppress("OPT_IN_USAGE")
    single<CoroutineScope> { GlobalScope }
    single<CoroutineDispatcher>(named(DISPATCHER_MAIN)) { Dispatchers.Main }
    single(named(DISPATCHER_IO)) { Dispatchers.Default }
    single(named(DISPATCHER_UNCONFINED)) { Dispatchers.Unconfined }
    single(named(DISPATCHER_DEFAULT)) { Dispatchers.Default }
}
