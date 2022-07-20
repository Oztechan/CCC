package com.oztechan.ccc.common.di.modules

import com.oztechan.ccc.common.di.modules.submodules.apiModule
import com.oztechan.ccc.common.di.modules.submodules.dataSourceModule
import com.oztechan.ccc.common.di.modules.submodules.databaseModule
import com.oztechan.ccc.common.di.modules.submodules.dispatcherModule
import com.oztechan.ccc.common.di.modules.submodules.serviceModule
import com.oztechan.ccc.common.di.modules.submodules.settingsModule

val commonModules = listOf(
    apiModule,
    databaseModule,
    dataSourceModule,
    dispatcherModule,
    serviceModule,
    settingsModule
)
