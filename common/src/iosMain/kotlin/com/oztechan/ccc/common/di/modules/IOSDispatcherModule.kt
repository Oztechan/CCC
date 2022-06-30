package com.oztechan.ccc.common.di.modules

import com.oztechan.ccc.common.di.DISPATCHER_DEFAULT
import com.oztechan.ccc.common.di.DISPATCHER_IO
import com.oztechan.ccc.common.di.DISPATCHER_MAIN
import com.oztechan.ccc.common.di.DISPATCHER_UNCONFINED
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val dispatcherModule: Module = module {
    single(named(DISPATCHER_MAIN)) { Dispatchers.Main }
    single(named(DISPATCHER_IO)) { Dispatchers.Default }
    single(named(DISPATCHER_UNCONFINED)) { Dispatchers.Unconfined }
    single(named(DISPATCHER_DEFAULT)) { Dispatchers.Default }
}
