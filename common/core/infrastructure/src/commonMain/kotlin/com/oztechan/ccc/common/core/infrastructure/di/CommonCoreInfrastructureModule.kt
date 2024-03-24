package com.oztechan.ccc.common.core.infrastructure.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

// names
const val DISPATCHER_IO = "DISPATCHER_IO"
const val DISPATCHER_MAIN = "DISPATCHER_MAIN"
const val DISPATCHER_UNCONFINED = "DISPATCHER_UNCONFINED"
const val DISPATCHER_DEFAULT = "DISPATCHER_DEFAULT"

@Suppress("OPT_IN_USAGE")
val commonCoreInfrastructureModule: Module = module {
    single<CoroutineScope> { GlobalScope }
    single<CoroutineDispatcher>(named(DISPATCHER_MAIN)) { Dispatchers.Main }
    single(named(DISPATCHER_IO)) { Dispatchers.IO }
    single(named(DISPATCHER_UNCONFINED)) { Dispatchers.Unconfined }
    single(named(DISPATCHER_DEFAULT)) { Dispatchers.Default }
}
