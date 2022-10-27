package com.oztechan.ccc.client.di.module

import com.oztechan.ccc.client.di.module.submodule.repositoryModule
import com.oztechan.ccc.client.di.module.submodule.viewModelModule
import com.oztechan.ccc.common.di.module.commonModules
import com.oztechan.ccc.config.di.module.configModules
import org.koin.core.module.Module

val clientModules: List<Module> = buildList {
    add(viewModelModule)
    add(repositoryModule)
    addAll(configModules)
    addAll(commonModules)
}
