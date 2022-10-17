package com.oztechan.ccc.common.di.module

import com.oztechan.ccc.common.di.module.submodule.apiModule
import com.oztechan.ccc.common.di.module.submodule.dataSourceModule
import com.oztechan.ccc.common.di.module.submodule.databaseModule
import com.oztechan.ccc.common.di.module.submodule.dispatcherModule
import com.oztechan.ccc.common.di.module.submodule.serviceModule
import com.oztechan.ccc.common.di.module.submodule.settingsModule
import org.koin.core.module.Module

val commonModules: List<Module> = buildList {
    add(apiModule)
    add(databaseModule)
    add(dataSourceModule)
    add(dispatcherModule)
    add(serviceModule)
    add(settingsModule)
}
