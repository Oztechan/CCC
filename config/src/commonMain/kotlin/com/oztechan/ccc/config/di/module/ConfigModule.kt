package com.oztechan.ccc.config.di.module

import com.oztechan.ccc.config.di.module.submodule.configServiceModule
import org.koin.core.module.Module

val configModules: List<Module> = buildList {
    add(configServiceModule)
}
