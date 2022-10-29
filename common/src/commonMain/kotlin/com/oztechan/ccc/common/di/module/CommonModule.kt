package com.oztechan.ccc.common.di.module

import com.oztechan.ccc.common.di.module.submodule.apiModule
import com.oztechan.ccc.common.di.module.submodule.dataSourceModule
import com.oztechan.ccc.common.di.module.submodule.databaseModule
import com.oztechan.ccc.common.di.module.submodule.dispatcherModule
import com.oztechan.ccc.common.di.module.submodule.serviceModule
import org.koin.core.module.Module

val commonModules: List<Module> = buildList {
    add(databaseModule)
    add(dataSourceModule)

    add(apiModule)
    add(serviceModule)

    add(dispatcherModule)
}
