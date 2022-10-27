package com.oztechan.ccc.common.di.module

import com.oztechan.ccc.common.di.module.submodule.apiModule
import com.oztechan.ccc.common.di.module.submodule.dataSourceModule
import com.oztechan.ccc.common.di.module.submodule.databaseModule
import com.oztechan.ccc.common.di.module.submodule.dispatcherModule
import com.oztechan.ccc.common.di.module.submodule.serviceModule
import com.oztechan.ccc.common.di.module.submodule.settingsModule
import com.oztechan.ccc.common.di.module.submodule.storageModule
import org.koin.core.module.Module

val commonModules: List<Module> = buildList {
    add(settingsModule)
    add(storageModule)

    add(databaseModule)
    add(dataSourceModule)

    add(apiModule)
    add(serviceModule)

    add(dispatcherModule)
}
